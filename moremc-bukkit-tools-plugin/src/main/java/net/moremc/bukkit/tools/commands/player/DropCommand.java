package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.drop.DropInventory;

public class DropCommand
{
    private final ToolsPlugin plugin;

    public DropCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(value = { "drop", "stone" }, description = "Pokazuje dostępne dropy")
    public void handle(@Sender Player player) {
        new DropInventory().show(player);
    }
}
