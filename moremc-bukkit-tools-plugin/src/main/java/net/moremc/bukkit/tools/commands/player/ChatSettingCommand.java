package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.ChatSettingsInventory;

public class ChatSettingCommand
{
    private final ToolsPlugin plugin;

    public ChatSettingCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "ustawienia", "cc"}, description = "Otwiera GUI z ustawianiami chatu")
    public void handle(@Sender Player player) {
        new ChatSettingsInventory().show(player);
    }
}
