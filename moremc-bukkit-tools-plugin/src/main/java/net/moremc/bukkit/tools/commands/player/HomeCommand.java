package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.HomeInventory;

public class HomeCommand
{
    private final ToolsPlugin plugin;

    public HomeCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "home", "dom", "sethome", "ustawdom"}, description = "Otwiera GUI do zarzadzania domami")
    public void handle(@Sender Player player) {
        new HomeInventory().show(player);
    }
}