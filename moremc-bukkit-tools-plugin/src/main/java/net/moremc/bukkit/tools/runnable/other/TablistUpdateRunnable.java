package net.moremc.bukkit.tools.runnable.other;

import net.moremc.bukkit.tools.service.TabListService;
import org.bukkit.Bukkit;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.cache.entity.BukkitUser;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.Optional;

public class TablistUpdateRunnable implements Runnable
{

    private final TabListService tabListService = new TabListService();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    @Override
    public void run() {
        for (BukkitUser bukkitUser : bukkitCache.getBukkitUsers()) {
            tabListService.update(Optional.of(bukkitUser));
        }
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(ToolsPlugin.getInstance(), this, 1L, 100L);
    }
}
