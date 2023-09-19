package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class InviteGuildCommand extends GuildSubCommand {


    private final GuildPermissionData permissionData = CommunicatorPlugin.getInstance().getPermissionData();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();

    public InviteGuildCommand() {
        super("zapros", "invite");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");


        if (args.length < 1) {
            messageHelper.setMessage(MessageHelper.colored(  "&7Poprawne użycie&8: &d{COMMAND}")
                    .replace("{COMMAND}", "/g zapros <nick>")).send(SendMessageType.CHAT);
            return false;
        }


        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {

                if(!guildPlayer.hasPermission(guild, 32)){
                    messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej akcji poproś kogoś w gildii.")).send(SendMessageType.CHAT);
                    return;
                }
                String nickName = args[0];
                if(!this.userService.findByValueOptional(nickName).isPresent()){
                    messageHelper.setMessage(MessageHelper.colored( "&cPodany gracz nigdy nie był na serwerze.")).send(SendMessageType.CHAT);
                    return;
                }
                if(this.guildService.findGuildByNickName(nickName).isPresent()){
                    messageHelper.setMessage(MessageHelper.colored(  "&cPodany gracz posiada już gildię")).send(SendMessageType.CHAT);
                    return;
                }
                guild.getPlayerInvites().add(nickName);
                guild.synchronize(SynchronizeType.UPDATE);

                ClientSendMessagePacket packet = new ClientSendMessagePacket("&aOtrzymałeś zaproszenie od gildi&8[&c" + guild.getName() + "&8]\n&8>> &fAby dołączył wpisz&8: &a/g dolacz " + guild.getName(),
                        SendMessageReceiverType.PLAYER, SendMessageType.CHAT);
                packet.setNickNameTarget(nickName);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", packet);

                messageHelper.setMessage(MessageHelper.colored(   "&aPomyślnie wysłano zaproszenie do gildii dla&8: &2{PLAYER}")
                        .replace("{PLAYER}", nickName)).send(SendMessageType.TITLE);
            });

        });



        return false;
    }
}
