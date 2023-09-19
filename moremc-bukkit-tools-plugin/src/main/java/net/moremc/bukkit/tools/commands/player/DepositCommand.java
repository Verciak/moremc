package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.DepositInventory;

public class DepositCommand
{
    private final ToolsPlugin plugin;


    public DepositCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "deposit", "schowek", "depozyt"}, description = "")
    public void handle(@Sender Player player) {
        new DepositInventory().show(player);
    }
}