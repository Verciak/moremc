package net.moremc.guilds.listeners.region;

import net.moremc.guilds.event.guild.GuildPlayerMoveEvent;
import net.moremc.guilds.event.handler.guild.GuildPlayerMoveHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.service.entity.GuildService;

import java.util.Optional;

public class GuildRegionPlayerMoveEventHandler implements Listener {

    private final GuildService guildService = API.getInstance().getGuildService();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        Optional<GuildImpl> guildFrom = this.guildService.findGuildByLocation(from.getWorld().getName(), from.getBlockX(), from.getBlockZ());
        Optional<GuildImpl> guildTo = this.guildService.findGuildByLocation(to.getWorld().getName(), to.getBlockX(), to.getBlockZ());
        new GuildPlayerMoveHandler().accept(new GuildPlayerMoveEvent(player, guildFrom, guildTo));
    }
}
