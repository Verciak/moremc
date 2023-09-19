package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.user.UserPlayOutRequestPacket;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MessageCommand
{
    private final ToolsPlugin toolsPlugin;

    private final UserService userService;
    private final SectorService sectorService;

    public MessageCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.userService = API.getInstance().getUserService();
        this.sectorService = API.getInstance().getSectorService();
    }
    @Command(value = {"message", "msg"}, description = "")
    public void handle(@Sender Player player, @Name("nickName") String nickName, @Name("message") @Combined String message) {
        if (nickName.trim().isEmpty()) {
            throw new BladeExitMessage(MessageHelper.colored("&7Poprawne użycie&8: &d/msg <nick> <wiadomość>"));
        }
        if (message.trim().isEmpty()) {
            throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
        }
        Optional<User> optionalUser = this.userService.findByValueOptional(nickName);

        if(!optionalUser.isPresent()){
            player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodany gracz nigdy nie był na serwerze"));
            return;
        }
        if(nickName.equalsIgnoreCase(player.getName())){
            player.sendMessage(MessageHelper.translateText("&4Błąd: &cNie możesz wysłać wiadomości sam do siebie!"));
            return;
        }

        optionalUser.ifPresent(user -> {
            if(!this.sectorService.isOnlinePlayer(user.getNickName())){
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodany gracz jest aktualnie offline!"));
                return;
            }
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new UserPlayOutRequestPacket(user.getNickName(), API.getInstance().getSectorName()));
            Bukkit.getScheduler().runTaskLaterAsynchronously(ToolsPlugin.getInstance(), () -> {

                User userRequest = this.userService.findOrCreate(user.getNickName(), new User(user.getNickName(), user.getUuid()));
                userRequest.setLatestPrivateMessageNickName(player.getName());
                userRequest.getUserSector().setCanInteract(true);

                this.userService.synchronizeUser(SynchronizeType.UPDATE, userRequest);

                userRequest = this.userService.findOrCreate(user.getNickName(), new User(user.getNickName(), user.getUuid()));

                player.sendMessage(MessageHelper.translateText("&5&lMSG &8| &dJa &5&l► &d" + userRequest.getNickName() + "&8: &5" + message));
                ClientSendMessagePacket sendMessagePacket =  new ClientSendMessagePacket("&5&lMSG &8| &d" + player.getName() + " &5&l► &dJa&8: &5" + message, SendMessageReceiverType.PLAYER, SendMessageType.CHAT);
                sendMessagePacket.setNickNameTarget(userRequest.getNickName());
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", sendMessagePacket);
            }, 5L);
        });
    }
    @Command(value = {"replay", "r"}, description = "")
    public void handleReplay(@Sender Player player, @Name("message") @Combined  String message) {
        if (message.trim().isEmpty()) {
            throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
        }
        this.userService.findByValueOptional(player.getName()).ifPresent(userSender -> {

            if(userSender.getLatestPrivateMessageNickName() == null){
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cNie masz komu odpisać"));
                return;
            }

            Optional<User> optionalUser = this.userService.findByValueOptional(userSender.getLatestPrivateMessageNickName());

            if (!optionalUser.isPresent()) {
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodany gracz nigdy nie był na serwerze"));
                return;
            }

            optionalUser.ifPresent(user -> {
                if (!this.sectorService.isOnlinePlayer(user.getNickName())) {
                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cGracz jest aktualnie offline!"));
                    return;
                }
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new UserPlayOutRequestPacket(user.getNickName(), API.getInstance().getSectorName()));
                Bukkit.getScheduler().runTaskLaterAsynchronously(ToolsPlugin.getInstance(), () -> {


                    User userRequest = this.userService.findOrCreate(user.getNickName(), new User(user.getNickName(), user.getUuid()));
                    userRequest.setLatestPrivateMessageNickName(player.getName());
                    userRequest.getUserSector().setCanInteract(true);

                    this.userService.synchronizeUser(SynchronizeType.UPDATE, userRequest);

                    userRequest = this.userService.findOrCreate(user.getNickName(), new User(user.getNickName(), user.getUuid()));

                    player.sendMessage(MessageHelper.translateText("&5&lMSG &8| &dJa &5&l► &d" + userRequest.getNickName() + "&8: &d" + message));
                    ClientSendMessagePacket sendMessagePacket = new ClientSendMessagePacket("&5&lMSG &8| &d" + player.getName() + " &5&l► &dJa&8: &5" + message, SendMessageReceiverType.PLAYER, SendMessageType.CHAT);
                    sendMessagePacket.setNickNameTarget(userRequest.getNickName());
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", sendMessagePacket);
                }, 5L);
            });
        });
    }

}