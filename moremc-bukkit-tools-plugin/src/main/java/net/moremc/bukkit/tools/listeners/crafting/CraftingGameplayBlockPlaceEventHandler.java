package net.moremc.bukkit.tools.listeners.crafting;

import net.moremc.bukkit.tools.event.crafting.CraftingGameplayBlockPlaceEvent;
import net.moremc.bukkit.tools.event.gameplay.CraftingGameplayBlockPlaceHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class CraftingGameplayBlockPlaceEventHandler implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraftingBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled())return;

        new CraftingGameplayBlockPlaceHandler().accept(new CraftingGameplayBlockPlaceEvent(event.getPlayer(), event));
    }

}
