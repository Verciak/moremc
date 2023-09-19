package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Permission;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;

public class WorkBenchCommand
{
    private final ToolsPlugin plugin;

    public WorkBenchCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Permission("moremc.command.workbench")
    @Command(value = { "workbench", "wb" }, description = "Otwiera przenośny crafting")
    public void handle(@Sender Player player) {
        player.openWorkbench(player.getLocation(), true);
    }
}
