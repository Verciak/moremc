package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import net.moremc.bukkit.tools.service.CraftingService;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.CraftingInventory;

public class CraftingCommand
{
    private final ToolsPlugin plugin;
    private final CraftingService service;

    public CraftingCommand(ToolsPlugin plugin, CraftingService service) {
        this.plugin = plugin;
        this.service = service;
    }
    @Command(value = { "crafting", "craftingi", "receptury"}, description = "Otwiera GUI z dostępnymi craftingami")
    public void handle(@Sender Player player) {
        new CraftingInventory(1, service).show(player);
    }
}