package net.moremc.bukkit.api.inventory.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.InventoryHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class InventoryHelperImpl implements InventoryHelper {

    private final String inventoryName;
    private int inventorySlot;
    private InventoryType inventoryType;
    private final Map<String, Inventory> inventoryViewMap = new ConcurrentHashMap<>();

    protected abstract void initializeInventory(Player player, Inventory inventory);

    protected boolean tryInitializeInventoryWithResult(Player player, Inventory inventory) {
        return true;
    }

    public InventoryHelperImpl(String inventoryName, int inventorySlot){
        this.inventoryName = inventoryName;
        this.inventorySlot = inventorySlot;
        BukkitAPI.getInstance().getInventoryCache().add(this);
    }
    public InventoryHelperImpl(String inventoryName, InventoryType inventoryType){
        this.inventoryName = inventoryName;
        this.inventoryType = inventoryType;
    }
    @Override
    public void initialize() {

    }

    @Override
    public boolean show(Player player) {
        Inventory inventory = Bukkit.createInventory(player, this.inventorySlot, MessageHelper.colored(this.inventoryName));
        if (!this.tryInitializeInventoryWithResult(player, inventory)) {
            return false;
        }
        player.openInventory(inventory);
        this.initializeInventory(player, inventory);
        this.inventoryViewMap.put(player.getName(), inventory);
        return true;
    }

    @Override
    public void onClick(Player player, Consumer<InventoryClickEvent> event) {
        this.inventoryClickEventMap.put(player, event);
    }
    public void onDrag(Player player, Consumer<InventoryDragEvent> event) {
        this.inventoryDragEventMap.put(player, event);
    }

    @Override
    public void onClose(Player player, Consumer<InventoryCloseEvent> event) {
        this.inventoryCloseEventMap.put(player, event);
    }

    @Override
    public void onOpen(Player player, Consumer<InventoryOpenEvent> event) {
        this.inventoryOpenEventMap.put(player, event);
    }
    public Map<Player, Consumer<InventoryClickEvent>> getInventoryClickMap(){
        return this.inventoryClickEventMap;
    }
    public Map<Player, Consumer<InventoryOpenEvent>> getInventoryOpenMap(){
        return this.inventoryOpenEventMap;
    }
    public Map<Player, Consumer<InventoryCloseEvent>> getInventoryCloseMap(){
        return this.inventoryCloseEventMap;
    }
    public Map<Player, Consumer<InventoryDragEvent>> getInventoryDragMap(){
        return this.inventoryDragEventMap;
    }

    @Override
    public void setNextPageItem(Inventory inventory, int slot) {
        inventory.setItem(slot, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3).setName("&aNastępna strona")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=").toItemStack());
    }

    @Override
    public void setBackPageItem(Inventory inventory, int slot) {
        inventory.setItem(slot, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3).setName("&cPoprzednia strona")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=").toItemStack());
    }

    public void removeInventoryView(String nickName, Inventory inventory){
        this.inventoryViewMap.remove(nickName, inventory);
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }


    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public Map<String, Inventory> getInventoryViewMap() {
        return inventoryViewMap;
    }
}
