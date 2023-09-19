package net.moremc.master.controller.runnable.controller;

import net.moremc.api.API;
import net.moremc.api.nats.packet.controller.MasterUpdatePacket;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MasterUpdateRunnable implements Runnable
{
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public void start(){
        this.scheduledExecutorService.scheduleAtFixedRate(this, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller_update", new MasterUpdatePacket());
    }
}
