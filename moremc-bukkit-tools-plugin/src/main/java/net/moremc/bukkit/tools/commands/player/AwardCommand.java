package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.AwardInventory;

public class AwardCommand
{
    private final ToolsPlugin plugin;

    public AwardCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = {"award", "nagroda"}, description = "Otwiera GUI z nagrodami")
    public void handle(@Sender Player player) {
        new AwardInventory().show(player);
    }
}