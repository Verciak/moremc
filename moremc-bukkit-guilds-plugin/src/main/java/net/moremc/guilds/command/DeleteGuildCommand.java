package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class DeleteGuildCommand extends GuildSubCommand {

    private final GuildService guildService = API.getInstance().getGuildService();

    public DeleteGuildCommand() {
        super("usun", "delete");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {

                if(!guildPlayer.getPlayerType().equals(GuildPlayerType.OWNER)){
                    messageHelper.setMessage("&cNie jesteś zalożycielem gildii.").send(SendMessageType.TITLE);
                    return;
                }
                ClientSendMessagePacket packet = new ClientSendMessagePacket("&fGracz &d" + player.getName() + " &fusunął swoją gildię &8[&d" + guild.getName().toUpperCase() + "&8]",
                        SendMessageReceiverType.ALL, SendMessageType.CHAT);

                guild.synchronize(SynchronizeType.REMOVE);

                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", packet);
            });
        });
        return false;
    }
}
