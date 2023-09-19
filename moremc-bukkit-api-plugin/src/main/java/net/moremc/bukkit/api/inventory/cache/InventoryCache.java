package net.moremc.bukkit.api.inventory.cache;


import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryCache {


    private final List<InventoryHelperImpl> inventoryHelperList = new ArrayList<>();

    public void add(InventoryHelperImpl inventoryHelper){
        this.inventoryHelperList.add(inventoryHelper);
    }

    public Optional<InventoryHelperImpl> findInventoryHelperByView(String nickName){
        return this.inventoryHelperList
                .stream()
                .filter(inventoryHelper -> inventoryHelper.getInventoryViewMap().containsKey(nickName))
                .findFirst();
    }

    public List<InventoryHelperImpl> getInventoryHelperList() {
        return inventoryHelperList;
    }
}
