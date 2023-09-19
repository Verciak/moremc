package net.moremc.master.controller.runnable.sector;

import net.moremc.api.API;
import net.moremc.api.nats.packet.sector.SectorUpdatePacket;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SectorSynchronizeRunnable implements Runnable
{

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    private final SectorService sectorService = API.getInstance().getSectorService();

    public void start(){
        this.scheduledExecutorService.scheduleAtFixedRate(this, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (Sector sector : this.sectorService.getMap().values()) {
            if(sector.getInfo().getLatestInformation() <= System.currentTimeMillis()){
                sector.getInfo().setPlayerList(new ArrayList<>());
                sector.getInfo().setOnline(false);
                sector.getInfo().setTicksPerSeconds(-1.00);
            }
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_sectors", new SectorUpdatePacket(sector.getName(),
                    sector.getInfo().getTicksPerSeconds(),
                    sector.getInfo().getLatestInformation(),
                    sector.getInfo().isOnline(),
                    sector.getInfo().getPlayerList()));

        }
    }
}
