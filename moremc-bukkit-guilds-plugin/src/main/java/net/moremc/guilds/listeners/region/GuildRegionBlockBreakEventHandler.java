package net.moremc.guilds.listeners.region;

import net.moremc.guilds.event.guild.region.GuildRegionBlockBreakEvent;
import net.moremc.guilds.event.handler.guild.region.GuildRegionBlockBreakHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.GuildService;

public class GuildRegionBlockBreakEventHandler implements Listener {

    private final GuildService guildService = API.getInstance().getGuildService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        Block block = event.getBlock();

        this.guildService.findGuildByLocation(player.getLocation().getWorld().getName(), block.getX(), block.getZ()).ifPresent(guild -> {
            new GuildRegionBlockBreakHandler().accept(new GuildRegionBlockBreakEvent(player, guild, event));
        });
    }
}
