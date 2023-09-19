package net.moremc.guilds.runnable;

import org.bukkit.Bukkit;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.guilds.GuildsPlugin;

public class GuildPlayerPeriscopeUpdateRunnable implements Runnable{

    private final UserService userService = API.getInstance().getUserService();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {


            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {
                    if(!user.isPeriscope()){
                        bukkitUser.removeActionBar(UserActionBarType.PERISCOPE);
                        return;
                    }
                    bukkitUser.updateActionBar(UserActionBarType.PERISCOPE, "&a&lSHIFT &8>> &fAby wyjść z peryskopu.");
                });

            });

        });
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(GuildsPlugin.getInstance(), this, 1, 20L);
    }
}
