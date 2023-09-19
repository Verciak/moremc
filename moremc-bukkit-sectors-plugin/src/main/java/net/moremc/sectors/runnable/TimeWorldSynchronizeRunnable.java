package net.moremc.sectors.runnable;

import net.moremc.sectors.SectorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class TimeWorldSynchronizeRunnable implements Runnable{


    @Override
    public void run() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set day");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimer(SectorPlugin.getInstance(), this, 1L, 1000L);

        World world = Bukkit.getWorld("world");
        world.setTime(1000);
        world.setThunderDuration(0);
        world.setWeatherDuration(0);
    }
}
