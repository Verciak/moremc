package net.moremc.bukkit.tools.runnable.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.bukkit.tools.ToolsPlugin;

public class PlayerProtectionRunnable implements Runnable {

    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final UserService userService = API.getInstance().getUserService();

    @Override
    public void run() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.userService.findByValueOptional(onlinePlayer.getName()).ifPresent(user -> {
                this.bukkitCache.findBukkitUserByNickName(user.getNickName()).ifPresent(bukkitUser -> {
                    if(user.hasProtection()){
                        bukkitUser.updateActionBar(UserActionBarType.PROTECTION,
                                "&5&lOCHRONA&8(&a" + DataHelper.getTimeToString(user.getCooldownTime("protection")) + "&7, &a/ochrona off&8)");
                        return;
                    }
                    bukkitUser.removeActionBar(UserActionBarType.PROTECTION);
                });
            });
        }
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(ToolsPlugin.getInstance(), this, 0, 0L);
    }
}
