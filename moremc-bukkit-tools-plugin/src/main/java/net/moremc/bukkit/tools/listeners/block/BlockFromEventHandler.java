package net.moremc.bukkit.tools.listeners.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromEventHandler implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockFrom(BlockFromToEvent event){
        if(event.isCancelled())return;
        event.setCancelled(true);
    }
}
