package net.moremc.bukkit.tools.inventories.admin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;

public class AdminKitConfigurationItemsInventory extends InventoryHelperImpl {

    private final Kit kit;

    public AdminKitConfigurationItemsInventory(Kit kit) {
        super("&dKonfiguracja zestawu&8(&f" + kit.getName() + "&8)", 54);
        this.kit = kit;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        if(kit.getInventoryItemsSerialized().equalsIgnoreCase("null")){
            kit.setInventoryItemsSerialized(ItemSerializer.decodeItems(new ItemStack[54]));
        }
        inventory.setContents(ItemSerializer.encodeItem(kit.getInventoryItemsSerialized()));


        this.onClose(player, event -> {
            kit.setInventoryItemsSerialized(ItemSerializer.decodeItems(event.getInventory().getContents()));
            kit.synchronize(SynchronizeType.UPDATE);

            player.sendMessage(MessageHelper.translateText("&dPomyślnie &fskonfigurwano zestaw &8(&d" + kit.getName() + "&8)"));;
        });
    }
}
