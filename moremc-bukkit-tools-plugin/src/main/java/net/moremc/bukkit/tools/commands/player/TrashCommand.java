package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class TrashCommand
{
    private final ToolsPlugin plugin;

    public TrashCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "trash", "kosz", "smietnik" }, description = "Otwiera śmietnik")
    public void handle(@Sender Player player) {
        player.openInventory(Bukkit.createInventory(player, 54, MessageHelper.colored("&fŚmietnik")));
    }
}
