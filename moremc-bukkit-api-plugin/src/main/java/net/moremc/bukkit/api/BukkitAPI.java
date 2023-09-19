package net.moremc.bukkit.api;

import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.event.BukkitUserInitializeEventHandler;
import net.moremc.bukkit.api.helper.ReflectionHelper;
import net.moremc.bukkit.api.inventory.cache.InventoryCache;
import net.moremc.bukkit.api.inventory.handler.InventoryHelperHandler;
import net.moremc.bukkit.api.runnable.ActionBarInformationRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.moremc.bukkit.api.schematic.SchematicFactory;

import java.util.HashMap;
import java.util.Map;

public class BukkitAPI extends JavaPlugin {

    private static BukkitAPI instance;
    private BukkitCache bukkitCache;
    private InventoryCache inventoryCache;
    private Map<String, SchematicFactory> schematicFactoryMap;

    public static BukkitAPI getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new ReflectionHelper().initialize();
        this.bukkitCache = new BukkitCache();
        this.inventoryCache = new InventoryCache();

        this.schematicFactoryMap = new HashMap<>();
        this.schematicFactoryMap.put("ender_stone", new SchematicFactory());
        this.schematicFactoryMap.put("obsidian", new SchematicFactory());
        this.schematicFactoryMap.put("nether", new SchematicFactory());
        this.schematicFactoryMap.put("sand", new SchematicFactory());
        this.schematicFactoryMap.put("pumpkin", new SchematicFactory());

        Bukkit.getPluginManager().registerEvents(new BukkitUserInitializeEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryHelperHandler(), this);

        new ActionBarInformationRunnable().start();
    }

    public InventoryCache getInventoryCache() {
        return inventoryCache;
    }

    public Map<String, SchematicFactory> getSchematicFactoryMap() {
        return schematicFactoryMap;
    }

    public BukkitCache getBukkitCache() {
        return bukkitCache;
    }
}
