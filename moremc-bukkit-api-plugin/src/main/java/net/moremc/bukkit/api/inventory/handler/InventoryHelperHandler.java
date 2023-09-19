package net.moremc.bukkit.api.inventory.handler;

import net.moremc.bukkit.api.inventory.cache.InventoryCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.BukkitAPI;

public class InventoryHelperHandler implements Listener {

    private final InventoryCache inventoryCache = BukkitAPI.getInstance().getInventoryCache();


    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getInventory() == null)return;
    
        this.inventoryCache.findInventoryHelperByView(player.getName()).ifPresent(inventoryHelper -> {
            Inventory inventory = inventoryHelper.getInventoryViewMap().get(player.getName());

            if (inventory.equals(event.getInventory())) {
                if(inventoryHelper.getInventoryClickMap().containsKey(player) && inventoryHelper.getInventoryClickMap().get(player) != null){
                    if(event.getClickedInventory() == null){
                        return;
                    }
                    if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                        event.setCancelled(true);
                        return;
                    }
                    inventoryHelper.getInventoryClickMap().get(player).accept(event);
                }
            }
        });
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrag(InventoryDragEvent event) {
        if(event.isCancelled())return;
        Player player = (Player) event.getWhoClicked();

        this.inventoryCache.findInventoryHelperByView(player.getName()).ifPresent(inventoryHelper -> {
            Inventory inventory = inventoryHelper.getInventoryViewMap().get(player.getName());
            if (inventory != null) {
                if (inventoryHelper.getInventoryDragMap().containsKey(player) && inventoryHelper.getInventoryDragMap().get(player) != null) {
                    inventoryHelper.getInventoryDragMap().get(player).accept(event);
                }
            }
        });
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        this.inventoryCache.findInventoryHelperByView(player.getName()).ifPresent(inventoryHelper -> {
            Inventory inventory = inventoryHelper.getInventoryViewMap().get(player.getName());
            if(inventory != null){
                inventoryHelper.removeInventoryView(player.getName(), inventory);

                if(inventoryHelper.getInventoryCloseMap().containsKey(player) && inventoryHelper.getInventoryCloseMap().get(player) != null){
                    inventoryHelper.getInventoryCloseMap().get(player).accept(event);
                }
                inventoryHelper.getInventoryCloseMap().remove(player);
                inventoryHelper.getInventoryOpenMap().remove(player);
                inventoryHelper.getInventoryDragMap().remove(player);
                inventoryHelper.getInventoryClickMap().remove(player);
            }
        });
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpen(InventoryOpenEvent event){
        Player player = (Player) event.getPlayer();

        this.inventoryCache.findInventoryHelperByView(player.getName()).ifPresent(inventoryHelper -> {
            Inventory inventory = inventoryHelper.getInventoryViewMap().get(player.getName());
            if(inventory != null){


                if(inventoryHelper.getInventoryOpenMap().containsKey(player) && inventoryHelper.getInventoryOpenMap().get(player) != null){
                    inventoryHelper.getInventoryOpenMap().get(player).accept(event);
                }
            }
        });
    }
}
