package net.moremc.bukkit.tools.listeners.block;

import net.moremc.bukkit.tools.event.UserBlockBreakDropHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.sectors.event.UserBlockBreakEvent;

public class BlockBreakDropEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        new UserBlockBreakDropHandler().accept(new UserBlockBreakEvent(this.userService.findByValueOptional(player.getName()), event));
    }
}
