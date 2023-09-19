package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.ItemShopInventory;

public class ItemShopCommand
{
    private final ToolsPlugin toolsPlugin;

    public ItemShopCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
    }

    @Command(value = { "itemshop", "is", "odbierz" }, description = "Otwiera GUI z zakupionmi przedmiotami")
    public void handle(@Sender Player player) {
        new ItemShopInventory().show(player);
    }
}
