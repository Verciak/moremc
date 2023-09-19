package net.moremc.guilds.listeners.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import net.moremc.api.API;
import net.moremc.api.bukkit.BlockState;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.utilities.RandomUtilities;

import java.util.ArrayList;

public class EntityExplodeEventHandler implements Listener {

    private final GuildService guildService = API.getInstance().getGuildService();

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event){
        event.setCancelled(true);

        new ArrayList<Location>(){{
            final int centerX = event.getLocation().getBlockX(),
                    centerY = event.getLocation().getBlockY(),
                    centerZ = event.getLocation().getBlockZ();

            for (int x = centerX - 5; x <= centerX + 5; x++)
                for (int y = (centerY - 5); y < (centerY + 5); y++)
                    for (int z = centerZ - 5; z <= centerZ + 5; z++)
                        if ((centerX - x) * (centerX - x) + (centerZ - z) * (centerZ - z) + ((centerY - y) * (centerY - y)) < 5 * 5)
                            add(new Location(event.getLocation().getWorld(), x, y, z));
        }}.forEach(block -> {
            this.guildService.findGuildByLocation(block.getWorld().getName(), block.getBlockX(), block.getBlockZ()).ifPresent(guild -> {
                if(guild.getProtectionTime() >= System.currentTimeMillis()) {
                    event.setCancelled(true);
                    return;
                }
                if(guild.isInCentrum(new LocationSerializer(block.getWorld().getName(), block.getBlockX(), 0, block.getBlockZ(), 0), 7, 2, 6)){
                    event.setCancelled(true);
                    return;
                }
                Material material = block.getBlock().getType();
                switch (material) {
                    case BEDROCK:
                    case AIR:return;
                    case TNT:{
                        block.getBlock().setType(Material.AIR);
                        TNTPrimed tnt = block.getBlock().getWorld().spawn(block.getBlock().getLocation(), TNTPrimed.class);
                        tnt.setFuseTicks(0);
                        break;
                    }
                    case WATER:
                    case LAVA: {
                        if (RandomUtilities.getChance(60)){
                            guild.getRegeneration().getBlockStateList().add(new BlockState(block.getBlock().getState().getType().name(), block.getBlock().getState().getData().getData(),
                                    new LocationSerializer(block.getWorld().getName(), block.getBlockX(), block.getBlockY(), block.getBlockZ(),0)));
                            block.getBlock().setType(Material.AIR);
                        }
                        return;
                    }
                    default:
                        if (RandomUtilities.getChance(90)){
                            guild.getRegeneration().getBlockStateList().add(new BlockState(block.getBlock().getState().getType().name(), block.getBlock().getState().getData().getData(),
                                    new LocationSerializer(block.getWorld().getName(), block.getBlockX(), block.getBlockY(), block.getBlockZ(),0)));
                            block.getBlock().setType(Material.AIR);
                        }
                }
            });
        });
    }
}
