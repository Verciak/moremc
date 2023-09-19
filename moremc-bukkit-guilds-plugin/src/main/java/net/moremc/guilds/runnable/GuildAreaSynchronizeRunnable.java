package net.moremc.guilds.runnable;

import net.moremc.api.API;
import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.utilities.RandomUtilities;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GuildAreaSynchronizeRunnable implements Runnable{

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    private final SectorService sectorService = API.getInstance().getSectorService();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final int minimumDistance = 64 + 64;

    @Override
    public void run() {
        if(this.guildService.getGuildAreaList().size() >= 84)return;
        for (Sector sector : this.sectorService.getMap().values()) {
            if(sector.isSpawn() || sector.isTeleport())continue;

            int randomX = RandomUtilities.getRandInt(sector.getInfo().getLocationInfo().getMinX(), sector.getInfo().getLocationInfo().getMaxX());
            int randomZ = RandomUtilities.getRandInt(sector.getInfo().getLocationInfo().getMinZ(), sector.getInfo().getLocationInfo().getMaxZ());
            if(sector.getInfo().getLocationInfo().getDistanceToBorder(randomX, randomZ) <= 50)continue;

            for (GuildImpl guild : this.guildService.getMap().values()) {
                if (guild.getLocation() != null && Math.abs(guild.getLocation().getX() - randomX) <= this.minimumDistance && Math.abs(guild.getLocation().getZ() - randomZ) <= this.minimumDistance)
                    return;
            }
            if (!this.guildService.findGuildAreaByCord(randomX, randomZ).isPresent()) {
                this.guildService.getGuildAreaList().add(new GuildArea(this.guildService.getGuildAreaList().size() + 1, randomX, randomZ));
            }
        }
    }
    public void start() {
        this.scheduledExecutorService.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }
}
