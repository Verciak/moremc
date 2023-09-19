package net.moremc.bukkit.tools.listeners.player;

import net.moremc.bukkit.tools.helper.BackPackHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.moremc.api.API;
import net.moremc.api.entity.backpack.BackPack;
import net.moremc.api.service.entity.BackPackService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;


public class PlayerJoinEventHandler implements Listener {

    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final BackPackService backPackService = API.getInstance().getBackPackService();
    private final UserService userService = API.getInstance().getUserService();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


        if(!this.backPackService.findByValueOptional(player.getUniqueId()).isPresent()){
            BackPack backPack = new BackPack(player.getUniqueId());
            this.backPackService.getMap().put(backPack.getFirstOwnerUUID(), backPack);
            player.getInventory().addItem(BackPackHelper.getBackPackItem(backPack, backPack.getType()));
        }
    }
}
