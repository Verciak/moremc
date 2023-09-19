package net.moremc.bukkit.api.event;

import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.cache.entity.BukkitUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.moremc.bukkit.api.BukkitAPI;

public class BukkitUserInitializeEventHandler implements Listener {


    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!this.bukkitCache.findBukkitUserByNickName(player.getName()).isPresent()){
            BukkitUser bukkitUser = this.bukkitCache.create(player.getName());
            bukkitUser.init(player);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(this.bukkitCache::delete);
    }

}
