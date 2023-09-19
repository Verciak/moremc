package net.moremc.bukkit.tools.listeners.block;

import net.moremc.bukkit.tools.event.UserBlockPlaceDropHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.sectors.event.UserBlockPlaceEvent;

public class BlockPlaceDropEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        new UserBlockPlaceDropHandler().accept(new UserBlockPlaceEvent(this.userService.findByValueOptional(player.getName()), event));
    }
}
