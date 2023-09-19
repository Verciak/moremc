package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Permission;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;

public class EnderChestCommand
{
    private final ToolsPlugin plugin;

    public EnderChestCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Permission("moremc.command.enderchest")
    @Command(value = { "enderchest", "ec" }, description = "Otwiera przenośny enderchest")
    public void handle(@Sender Player player) {
        player.openInventory(player.getEnderChest());
    }
}
