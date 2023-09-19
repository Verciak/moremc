package net.moremc.bukkit.api.runnable;

import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.bukkit.api.BukkitAPI;

public class ActionBarInformationRunnable implements Runnable{

    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    public void start(){
        Bukkit.getScheduler().runTaskTimer(BukkitAPI.getInstance(), this, 0, 0);
    }

    @Override
    public void run() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            this.bukkitCache.findBukkitUserByNickName(onlinePlayer.getName()).ifPresent(user -> {
                user.getPlayerHelper().sendActionbar(String.join( "::", MessageHelper.colored(user.collectActiveActionBars())));
            });
        }
    }
}
