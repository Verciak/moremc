package net.moremc.sectors.handler.scenario;

import net.moremc.sectors.event.handler.UserSectorBlockBreakHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.sectors.event.UserBlockBreakEvent;
import net.moremc.sectors.event.UserBlockPlaceEvent;
import net.moremc.sectors.event.handler.UserSectorBlockPlaceHandler;

public class BlockBreakPlaceScenarioEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        new UserSectorBlockBreakHandler().accept(new UserBlockBreakEvent(this.userService.findByValueOptional(player.getName()), event));
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        new UserSectorBlockPlaceHandler().accept(new UserBlockPlaceEvent(this.userService.findByValueOptional(player.getName()), event));
    }
}
