package net.moremc.bukkit.api.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public interface InventoryHelper {


    /**
     * @parm inventoryClickEventMap, map all field onClick
     */
    Map<Player, Consumer<InventoryClickEvent>> inventoryClickEventMap = new ConcurrentHashMap<>();

    Map<Player, Consumer<InventoryDragEvent>> inventoryDragEventMap = new ConcurrentHashMap<>();

    /**
     * @parm inventoryOpenEventMap, map all field onOpen
     */
    Map<Player, Consumer<InventoryOpenEvent>> inventoryOpenEventMap = new ConcurrentHashMap<>();

    /**
     * @parm inventoryCloseEventMap, map all field onClose
     */
    Map<Player, Consumer<InventoryCloseEvent>> inventoryCloseEventMap = new ConcurrentHashMap<>();
    /**
     * @parm Method initialize add caching inventory and set paginate
     */
    void initialize();

    /**
     * @parmr Method show, show inventory for player
     */
    boolean show(Player player);

    /**
     * @parm Method onOpen accept event click if inventory equals this
     */
    void onClick(Player player, Consumer<InventoryClickEvent> event);

    /**
     * @parm Method onClose accept event close if inventory equals this
     */
    void onClose(Player player, Consumer<InventoryCloseEvent> event);


    /**
     * @parm Method onOpen accept event open if inventory equals this
     */
    void onOpen(Player player, Consumer<InventoryOpenEvent> event);
    /**
     * @parm Method setNextPageItem set item for next page click
     * @param slot
     */
    void setNextPageItem(Inventory inventory, int slot);

    /**
     * @parm Method setBackPageItem set item for back page click
     * @param slot
     */
    void setBackPageItem(Inventory inventory, int slot);

}
