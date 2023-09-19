package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class FriendlyFireGuildCommand extends GuildSubCommand {

    private final GuildService guildService = API.getInstance().getGuildService();

    public FriendlyFireGuildCommand() {
        super("pvp");
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

                if (!guildPlayer.hasPermission(guild, 31)) {
                    messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej akcji poproś kogoś w gildii.")).send(SendMessageType.CHAT);
                    return;
                }
                guild.setFriendlyFire(!guild.isFriendlyFire());
                guild.sendMessage("&fWalka w gildii została&8: " + (guild.isFriendlyFire() ? "&awłączona" : "&cwyłączona") + " &fprzez&8: &d" + player.getName());
            });
        });
        return false;
    }
}
