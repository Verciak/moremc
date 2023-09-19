package net.moremc.bukkit.tools.inventories.backpack;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.backpack.BackPack;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;

public class BackPackInventory extends InventoryHelperImpl
{
    private final BackPack backpack;

    public BackPackInventory(BackPack backPack) {
        super("&dPlecak: &f" + backPack.getBackpackUUID(), 54);
        this.backpack = backPack;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        if(backpack.getItems().equalsIgnoreCase("null")){
            backpack.setItems(ItemSerializer.decodeItems(new ItemStack[54]));
            player.sendMessage(MessageHelper.translateText("&fOtworzono &dpomyślnie &fplecak pierwszy raz."));
        }

        inventory.setContents(ItemSerializer.encodeItem(backpack.getItems()));

        onClose(player, close -> {

           Inventory closeInventory = close.getInventory();
           for (ItemStack itemContent : closeInventory.getContents()) {
               if(itemContent == null || itemContent.getType().equals(Material.AIR)) continue;
               if(itemContent.getType().equals(Material.SKULL_ITEM)){
                   closeInventory.removeItem(itemContent);
                   player.getInventory().addItem(itemContent);
               }
           }
           backpack.setItems(ItemSerializer.decodeItems(closeInventory.getContents()));
           player.sendMessage(MessageHelper.colored("&fPomyślnie &dzapisano &fzawartość plecaka&f!"));
        });
       player.openInventory(inventory);
    }
}
