package net.moremc.bukkit.tools.inventories.bazaar;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

public class BazaarPlayerInventory extends InventoryHelperImpl
{
    public BazaarPlayerInventory(String nick) {
        super("&dBazar: &d" + nick, 54);
    }
    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        //TODO
    }
}
