package net.moremc.guilds.command;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.GuildPlayer;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class JoinGuildCommand extends GuildSubCommand {

    private final GuildService guildService = API.getInstance().getGuildService();

    public JoinGuildCommand() {
        super("dolacz", "join");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");
        if (this.guildService.findGuildByNickName(player.getName()).isPresent()) {
            messageHelper.setMessage(MessageHelper.colored("&cPosiadasz już gildię")).send(SendMessageType.CHAT);
            return false;
        }

        if(!this.guildService.findGuildByInvite(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz zaproszenia do gildii")).send(SendMessageType.CHAT);
            return false;
        }
        this.guildService.findGuildByInvite(player.getName()).ifPresent(guild -> {

            guild.findGuildPlayerByInvite(player.getName()).ifPresent(playerGuild -> {
                guild.getPlayerInvites().remove(playerGuild);
            });

            guild.findDefaultPermission().ifPresent(guildPermission -> {
                GuildPlayer guildPlayer = new GuildPlayer(player.getName(), GuildPlayerType.MEMBER);
                guildPlayer.getPermission().getPermissions().addAll(guildPermission.getPermissions());
                guild.addPlayer(guildPlayer);
                guildPermission.getPlayers().add(player.getName());
                guild.synchronize(SynchronizeType.UPDATE);

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
                player.sendTitle("", MessageHelper.translateText("&2&l♚ &aPomyślnie dołączono do gildii&8[&a" + guild.getName() + "&8] &2&l♚"));

                ClientSendMessagePacket packet = new ClientSendMessagePacket("&c&l♚ &fGracz&8: &c" + player.getName() + " &fdołączył do gildii&8[&c" + guild.getName() + "&8]",
                        SendMessageReceiverType.ALL, SendMessageType.CHAT);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", packet);

            });


        });




        return false;
    }
}
