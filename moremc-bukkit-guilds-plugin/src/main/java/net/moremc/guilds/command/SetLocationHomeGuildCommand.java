package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class SetLocationHomeGuildCommand extends GuildSubCommand {


    private final GuildService guildService = API.getInstance().getGuildService();

    public SetLocationHomeGuildCommand() {
        super("ustawdom", "sethome", "ustawbaze");
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

                if(!GuildPlayerType.hasPermissionMaster(guildPlayer.getPlayerType())){
                    messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej akcji w gildii")).send(SendMessageType.CHAT);
                    return;
                }
                if(guild.getLocationHome() == null){
                    messageHelper.setMessage(MessageHelper.colored("&cTwoja gildia wymaga konfiguracja&8: &4/g konfiguracja")).send(SendMessageType.CHAT);
                    return;
                }
                if(!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                    messageHelper.setMessage(MessageHelper.colored("&cMusisz być na terenie swojej gildii.")).send(SendMessageType.CHAT);
                    return;
                }
                guild.setLocationHome(new LocationSerializer(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), 0));
                guild.sendMessage(MessageHelper.colored("&fGracz &d{PLAYER} &fustawił nowy lokacje domu: &d{X}&7, &d{Y}&7, &d{Z}")
                        .replace("{PLAYER}", player.getName())
                        .replace("{X}", String.valueOf(player.getLocation().getX()))
                        .replace("{Y}", String.valueOf(player.getLocation().getY()))
                        .replace("{Z}", String.valueOf(player.getLocation().getZ())));
                guild.synchronize(SynchronizeType.UPDATE);
            });
        });
        return false;
    }
}
