package net.moremc.bukkit.tools.inventories.backpack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.backpack.type.BackPackColorType;
import net.moremc.api.service.entity.BackPackService;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.helper.BackPackHelper;
import net.moremc.bukkit.tools.service.CustomItemService;

import java.util.Arrays;
import java.util.UUID;

public class BackPackColorInventory extends InventoryHelperImpl
{
    private final BackPackService service = API.getInstance().getBackPackService();
    private final CustomItemService customItemService = ToolsPlugin.getInstance().getCustomItemService();

    public BackPackColorInventory() {
        super("&dZmiena koloru plecaka", 54);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(4, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&dZmiena koloru plecaka")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIzMDIyMzJkODgyMjRlZTU3ODZhNjhjN2NhYzIwMzIxMWZjNmM4YjIzYmNmNDhkYzg0NWM4MDY1YjBjY2I1OSJ9fX0=")
                .toItemStack()
        );
        inventory.setItem(49, new ItemBulider(Material.DARK_OAK_FENCE_GATE).setName("&cWyjście").toItemStack());

        Arrays.stream(BackPackColorType.values()).forEach(backPackColor -> {
            inventory.addItem(new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                    .setName("&dKolor: &f" + backPackColor.getName())
                    .setOwnerUrl(backPackColor.getValue())
                    .setIdentifyItem("backpack-color", backPackColor.name())
                    .toItemStack());
        });

        onClick(player, event -> {
            event.setCancelled(true);
            if(event.getSlot() == 49) {
                player.closeInventory();
                return;
            }
            ItemStack item = player.getItemInHand();
            NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();

            if (item == null || item.getType().equals(Material.AIR) || tag == null || !tag.hasKey("backpack-uuid")) {
                player.sendMessage(MessageHelper.colored("&cItem który trzymasz w ręce nie jest plecakiem!"));
                player.closeInventory();
                return;
            }
            ItemStack candyItem = customItemService.find("candy");

            if (!player.getInventory().containsAtLeast(candyItem, 1)) {
                player.sendMessage(MessageHelper.colored("&cAby zmnić kolor plecaka musisz posiadac &7CUKIERKA &c!"));
                player.closeInventory();
                return;
            }
            BackPackColorType color = BackPackColorType.valueOf(ItemIdentityHelper.getItemIdentityTagName(event.getCurrentItem(), "backpack-color"));
            UUID backPackUUID = UUID.fromString(tag.getString("backpack-uuid"));

            service.findByValueOptional(backPackUUID).ifPresent(backPack -> {
                if (!backPack.getOwnerUUID().equals(player.getUniqueId())) {
                    player.sendMessage(MessageHelper.colored("&cNie jesteś właścicielem tego plecaka"));
                    return;
                }
                player.getInventory().removeItem(candyItem);
                player.getInventory().setItem(player.getInventory().first(item), BackPackHelper.getBackPackItem(backPack, color));

                player.sendTitle("", MessageHelper.colored("&fPomyślnie zmieniono kolor plecaka na &d" + color.getName()));
                player.closeInventory();
            });
        });
    }
}
