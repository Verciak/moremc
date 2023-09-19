package net.moremc.guilds.listeners.region;

import net.moremc.guilds.event.guild.region.GuildRegionBlockPlaceEvent;
import net.moremc.guilds.event.handler.guild.region.GuildRegionBlockPlaceHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.GuildService;

public class GuildRegionBlockPlaceEventHandler implements Listener {


    private final GuildService guildService = API.getInstance().getGuildService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled())return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        this.guildService.findGuildByLocation(player.getLocation().getWorld().getName(), block.getX(), block.getZ()).ifPresent(guild -> {
            new GuildRegionBlockPlaceHandler().accept(new GuildRegionBlockPlaceEvent(player, guild, event));
        });
    }
}
