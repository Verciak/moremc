package net.moremc.bukkit.tools.service;

import net.moremc.bukkit.tools.data.Crafting;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CraftingService
{
    private final Map<String, Crafting> craftings = new ConcurrentHashMap<>();

    private static CustomItemService customItem = ToolsPlugin.getInstance().getCustomItemService();

    public void load() {
        craftings.put("cobblex", new Crafting(1, customItem.find("cobblex"), new HashMap<Integer, ItemStack>() {{
            put(10, new ItemStack(Material.COBBLESTONE, 64));
            put(11, new ItemStack(Material.COBBLESTONE, 64));
            put(12, new ItemStack(Material.COBBLESTONE, 64));
            put(19, new ItemStack(Material.COBBLESTONE, 64));
            put(20, new ItemStack(Material.COBBLESTONE, 64));
            put(21, new ItemStack(Material.COBBLESTONE, 64));
            put(28, new ItemStack(Material.COBBLESTONE, 64));
            put(29, new ItemStack(Material.COBBLESTONE, 64));
            put(30, new ItemStack(Material.COBBLESTONE, 64));
        }}));
        craftings.put("stonework", new Crafting(2, customItem.find("stonework"), new HashMap<Integer, ItemStack>() {{
            put(10, new ItemStack(Material.STONE, 1));
            put(11, new ItemStack(Material.STONE, 1));
            put(12, new ItemStack(Material.STONE, 1));
            put(19, new ItemStack(Material.STONE, 1));
            put(20, new ItemStack(Material.GOLD_PICKAXE, 1));
            put(21, new ItemStack(Material.STONE, 1));
            put(28, new ItemStack(Material.STONE, 1));
            put(29, new ItemStack(Material.STONE, 1));
            put(30, new ItemStack(Material.STONE, 1));
        }}));
        craftings.put("golden_apple", new Crafting(3, customItem.find("golden_apple"), new HashMap<Integer, ItemStack>() {{
            put(11, new ItemStack(Material.GOLD_INGOT, 1));
            put(19, new ItemStack(Material.GOLD_INGOT, 1));
            put(20, new ItemStack(Material.APPLE, 1));
            put(21, new ItemStack(Material.GOLD_INGOT, 1));
            put(29, new ItemStack(Material.GOLD_INGOT, 1));
        }}));
        craftings.put("test", new Crafting(4, new ItemStack(Material.DIRT), new HashMap<Integer, ItemStack>() {{
            put(20, new ItemStack(Material.STICK, 64));
        }}));
    }
    public Optional<Crafting> find(int page) {
        return craftings.values().stream().filter(crafting -> crafting.getPage() == page).findFirst();
    }
    public Map<String, Crafting> getCraftings() {
        return craftings;
    }
}
