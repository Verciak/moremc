package net.moremc.bukkit.tools.data;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Crafting
{
    private final int page;

    private final ItemStack resultItem;
    private final Map<Integer, ItemStack> requiredItems;

    public Crafting(int page, ItemStack resultItem, Map<Integer, ItemStack> requiredItems) {
        this.page = page;
        this.resultItem = resultItem;
        this.requiredItems = requiredItems;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    public int getPage() {
        return page;
    }

    public Map<Integer, ItemStack> getRequiredItems() {
        return requiredItems;
    }
}
