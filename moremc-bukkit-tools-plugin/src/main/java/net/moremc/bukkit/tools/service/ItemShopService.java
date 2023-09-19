package net.moremc.bukkit.tools.service;

import net.moremc.bukkit.tools.data.ItemShop;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemShopService 
{
    private final List<ItemShop> itemShops = new ArrayList<>();

    private final CustomItemService service = ToolsPlugin.getInstance().getCustomItemService();

    public void load() {
        itemShops.add(new ItemShop("VIP",  "group {PLAYER} VIP"));
        itemShops.add(new ItemShop("case_16", "ais addDatabase {PLAYER} case 16"));
        itemShops.add(new ItemShop("case_32", "ais addDatabase {PLAYER} case 32"));
    }
    public Optional<ItemShop> find(String service) {
        return itemShops.stream().filter(itemShop -> itemShop.getName().equalsIgnoreCase(service)).findFirst();
    }

    public List<ItemShop> getItemShops() {
        return itemShops;
    }
}
