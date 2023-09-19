package net.moremc.bukkit.tools.inventories.backpack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.service.entity.BackPackService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.tools.helper.BackPackHelper;

import java.util.UUID;

public class BackPackSlittingInventory extends InventoryHelperImpl
{
    private final BackPackService service = API.getInstance().getBackPackService();

    public BackPackSlittingInventory() {
        super("&dRozcinanie plecka", 27);
    }
    @Override
    protected void initializeInventory(Player player, Inventory inventory) {

        onDrag(player, event -> {
            int slot = event.getRawSlots().stream().findFirst().orElse(0);

            if(slot == 13) {
                NBTTagCompound tag = CraftItemStack.asNMSCopy(event.getCursor()).getTag();
                if(!tag.hasKey("backpack-uuid")) {
                    player.closeInventory();
                    player.sendMessage(MessageHelper.colored("&cItem który wkładasz do &7ekwipunku &cnie jest plecakiem!"));
                    return;
                }
                UUID backPackUUID = UUID.fromString(tag.getString("backpack-uuid"));
                service.findByValueOptional(backPackUUID).ifPresent(backPack -> {
                    if(backPack.getOwnerUUID().equals(player.getUniqueId())) {
                        player.sendMessage(MessageHelper.colored("&cTen plecak jest już twój!"));
                        return;
                    }
                    backPack.setOwnerUUID(player.getUniqueId());

                    player.closeInventory();
                    player.getInventory().addItem(BackPackHelper.getBackPackItem(backPack, backPack.getType()));
                    player.sendMessage(MessageHelper.colored(""));
                });
            }
        });
        onClick(player, event -> {
            if(event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                event.setCancelled(true);
            }
        });
    }
}
