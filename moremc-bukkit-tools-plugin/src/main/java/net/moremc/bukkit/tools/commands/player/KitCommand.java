package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.kit.KitSelectInventory;

public class KitCommand
{
    private ToolsPlugin plugin;

    public KitCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "kit" }, description = "Otwiera GUI z dostępnymi zestawami")
    public void handle(@Sender Player player) {
        new KitSelectInventory().show(player);
    }
}