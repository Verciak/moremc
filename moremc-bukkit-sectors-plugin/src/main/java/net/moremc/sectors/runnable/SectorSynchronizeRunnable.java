package net.moremc.sectors.runnable;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.moremc.api.API;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.sector.SectorUpdatePacket;
import org.bukkit.Bukkit;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SectorSynchronizeRunnable implements Runnable {


    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public void start() {
        this.scheduledExecutorService.scheduleAtFixedRate(this, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        API.getInstance().getSectorService().getCurrentSector().ifPresent(sector -> {
            sector.getInfo().getPlayerList().clear();
            Bukkit.getOnlinePlayers().forEach(player -> sector.getInfo().getPlayerList().add(player.getName()));
            sector.getInfo().setTicksPerSeconds(MinecraftServer.getServer().recentTps[0]);
            sector.getInfo().setLatestInformation(System.currentTimeMillis() + TimeType.SECOND.getTime(2));
            sector.getInfo().setOnline(true);

            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new SectorUpdatePacket(sector.getName(),
                    sector.getInfo().getTicksPerSeconds(),
                    sector.getInfo().getLatestInformation(),
                    sector.getInfo().isOnline(),
                    sector.getInfo().getPlayerList()));

        });
    }
}
