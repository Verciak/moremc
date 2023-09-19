package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import net.moremc.bukkit.tools.inventories.achievement.AchievementInventory;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;

public class AchievementCommand
{
    private final ToolsPlugin plugin;

    public AchievementCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "achievement", "osiagniecia", "os" }, description = "Otwiera GUI z osiągnieciami")
    public void handle(@Sender Player player) {
        new AchievementInventory().show(player);
    }
}