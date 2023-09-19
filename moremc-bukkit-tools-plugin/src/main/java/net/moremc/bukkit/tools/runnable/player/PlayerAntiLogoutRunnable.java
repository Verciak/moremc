package net.moremc.bukkit.tools.runnable.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.text.DecimalFormat;

public class PlayerAntiLogoutRunnable implements Runnable{


    private final UserService userService = API.getInstance().getUserService();
    private final DecimalFormat decimalFormat = new DecimalFormat("##");
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    @Override
    public void run() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.userService.findByValueOptional(onlinePlayer.getName()).ifPresent(user -> {
                this.bukkitCache.findBukkitUserByNickName(user.getNickName()).ifPresent(bukkitUser -> {
                    if(!user.getUserAntiLogout().hasAntiLogout()){
                        long time = user.getUserAntiLogout().getAntiLogoutTime();
                        double percent = (((time - System.currentTimeMillis()) / 1000.0) * 10.0) / 3;

                        bukkitUser.updateActionBar(UserActionBarType.ANTI_LOGOUT,
                                "&d&lAntyLogout&8(&d" + DataHelper.getTimeToString(time) + "&7, " +
                                        MessageHelper.progress((int) (100 - percent / 10 - 89),
                                                (int) percent / 10) + "&7, &d" + this.decimalFormat.format(percent) + "&5%&8)" );
                        return;
                    }
                    bukkitUser.removeActionBar(UserActionBarType.ANTI_LOGOUT);
                });
            });
        }
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(ToolsPlugin.getInstance(), this, 0, 0L);
    }

}
