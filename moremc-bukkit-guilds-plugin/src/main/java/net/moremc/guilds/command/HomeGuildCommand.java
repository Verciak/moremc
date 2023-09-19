package net.moremc.guilds.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.TeleportHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class HomeGuildCommand extends GuildSubCommand {


    private final GuildService guildService = API.getInstance().getGuildService();

    public HomeGuildCommand() {
        super("dom", "home", "baza");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored(" &cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            if(guild.getLocationHome() == null){
                messageHelper.setMessage(MessageHelper.colored("&cTwoja gildia wymaga konfiguracja &7/g konfiguracja")).send(SendMessageType.CHAT);
                return;
            }
            TeleportHelper.teleport(player, System.currentTimeMillis() + TimeType.SECOND.getTime(5), new Location(Bukkit.getWorld("world"),guild.getLocationHome().getX(), guild.getLocationHome().getY(), guild.getLocationHome().getZ()));
        });


        return false;
    }
}
