package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.teleport.TeleportPlayerRequestPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.user.UserPlayOutRequestPacket;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeleportCommand
{
    private ToolsPlugin plugin;

    private final UserService userService;
    private final SectorService sectorService;

    public TeleportCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.userService = API.getInstance().getUserService();
        this.sectorService = API.getInstance().getSectorService();

    }
    @Command(value = {"teleportrequest", "tpa"}, description = "")
    public void handleRequest(@Sender Player player, @Name("nick") String nickName) {
        Optional<User> optionalUser = this.userService.findByValueOptional(nickName);

        if(!optionalUser.isPresent()){
            player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodany gracz nigdy nie był na serwerze"));
            return;
        }
        if(nickName.equalsIgnoreCase(player.getName())){
            player.sendMessage(MessageHelper.translateText("&4Błąd: &cNie możesz wysłać prośby o teleport sam do siebie!"));
            return;
        }
        optionalUser.ifPresent(user -> {
            if(!this.sectorService.isOnlinePlayer(user.getNickName())){
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodany gracz jest aktualnie offline!"));
                return;
            }
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller",new UserPlayOutRequestPacket(user.getNickName(), API.getInstance().getSectorName()));
            player.sendMessage(MessageHelper.translateText("&5&lTELEPORTACJA&8: &fTrwa wysyłanie prośby o teleportacje do gracza: &d" + user.getNickName()));
            Bukkit.getScheduler().runTaskLaterAsynchronously(CommunicatorPlugin.getInstance(), () -> {


                User userRequest = this.userService.findOrCreate(user.getNickName(), new User(user.getNickName(), user.getUuid()));
                userRequest.getPlayersTeleportRequest().add(player.getName());
                userRequest.getUserSector().setCanInteract(true);

                this.userService.synchronizeUser(SynchronizeType.UPDATE, userRequest);

                userRequest = this.userService.findOrCreate(user.getNickName(), new User(user.getNickName(), user.getUuid()));

                player.sendMessage(MessageHelper.translateText("&5&lTELEPORTACJA&8: &fGracz: &d" + user.getNickName() + " &fotrzymał prośbe o teleportacje."));

                ClientSendMessagePacket sendMessagePacket =  new ClientSendMessagePacket(
                        "&5&lTELEPORTACJA&8: &fOtrzymałeś prośbe o teleportacje od gracza: &d" + player.getName() + "\n" +
                                "&fAby zaakceptować prośbe o teleportacje: &d/tpaccept " + player.getName() + "\n" +
                                "&fPosiadasz łącznie już&8(&d" +  userRequest.getPlayersTeleportRequest().size() + "&7, &fprósb&8)", SendMessageReceiverType.PLAYER,
                        SendMessageType.CHAT);
                sendMessagePacket.setNickNameTarget(userRequest.getNickName());
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", sendMessagePacket);

            }, 5L);
        });
    }
    @Command(value = {"teleportaccept", "tpaccept"}, description = "")
    public void handleAccept(@Sender Player player, @Name("nick/*") String argument) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if (argument.equalsIgnoreCase("*")) {
                if (user.getPlayersTeleportRequest().size() <= 0) {
                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cNie posiadasz żadnej prośby o teleportacje!"));
                    return;
                }
                user.getPlayersTeleportRequest().forEach(nickName -> {
                    player.sendMessage(MessageHelper.translateText("&5&lTELEPORTACJA&8: &fZaakceptowałeś wszystkie prośby o teleportacje!"));

                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", new TeleportPlayerRequestPacket(nickName, player.getName(), System.currentTimeMillis() + TimeType.SECOND.getTime(10)));
                });
                user.getPlayersTeleportRequest().clear();
                return;
            }
            String nickName = argument;
            if (!this.sectorService.isOnlinePlayer(nickName)) {
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodany gracz jest aktualnie offline!"));
                return;
            }
            if (!user.getPlayersTeleportRequest().contains(nickName)) {
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cNie masz prośby o teleportacje od tego gracza!"));
                return;
            }
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", new TeleportPlayerRequestPacket(nickName, player.getName(), System.currentTimeMillis() + TimeType.SECOND.getTime(10)));
            player.sendMessage(MessageHelper.translateText("&5&lTELEPORTACJA&8: &fZaakceptowałeś prośbe o teleport od: &d" + nickName));
        });

    }
}