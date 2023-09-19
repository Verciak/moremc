package net.moremc.bukkit.tools.inventories.admin;

import net.moremc.api.entity.kit.Kit;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdminKitConfigurationGlassInventory extends InventoryHelperImpl {

    private final Kit kit;

    public AdminKitConfigurationGlassInventory(Kit kit) {
        super("&dKonfiguracja zestawu&8(&f" + kit.getName() + "&8)", 54);
        this.kit = kit;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        if(kit.getInventoryGlassSerialized().equalsIgnoreCase("null")){
            kit.setInventoryGlassSerialized(ItemSerializer.decodeItems(new ItemStack[54]));
        }
        inventory.setContents(ItemSerializer.encodeItem(kit.getInventoryGlassSerialized()));

        this.onClose(player, event -> {
            kit.setInventoryGlassSerialized(ItemSerializer.decodeItems(event.getInventory().getContents()));
            kit.synchronize(SynchronizeType.UPDATE);

            player.sendMessage(MessageHelper.translateText("&dPomyślnie &fskonfigurwano zestaw &8(&d" + kit.getName() + "&8)"));
        });
    }
}
