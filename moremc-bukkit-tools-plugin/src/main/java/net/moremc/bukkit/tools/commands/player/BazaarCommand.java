package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.bazaar.BazaarInventory;

public class BazaarCommand
{
    private final ToolsPlugin toolsPlugin;

    public BazaarCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
    }
    @Command(value = { "bazaar", "bazar", "rynek", "sprzedaj", "sklep", "market" }, description = "Pozwala sprzedawac przedmioty")
    public void handle(@Sender Player player){
        BazaarInventory bazaarInventory = new BazaarInventory(0);
        bazaarInventory.show(player);
    }
}
