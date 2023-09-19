package net.moremc.bukkit.tools.inventories.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class AdminPanelInventory extends InventoryHelperImpl
{
    public AdminPanelInventory() {
        super("&dPanel Adminstracyjny", 54);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(10, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).setName("&dGildie").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNkMTQ1NjFiYmQwNjNmNzA0MjRhOGFmY2MzN2JmZTljNzQ1NjJlYTM2ZjdiZmEzZjIzMjA2ODMwYzY0ZmFmMSJ9fX0=").toItemStack());
        inventory.setItem(11, new ItemBulider(Material.DIAMOND_SWORD).setName("&dKity").toItemStack());
        inventory.setItem(12, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).setName("&dCase").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmYwMDk5MWFlNzljYWMzNGUzNTgzNTdjMTNkYjY5Y2E2ZmVkODJiOGE1OWE5MjE4OWYzZmU0ZmRiMmU0ZTU4OSJ9fX0=").toItemStack());
        inventory.setItem(13, new ItemBulider(Material.DIAMOND_CHESTPLATE).setName("&dDiamentowe rzeczy").toItemStack());

        onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 10) {
                new AdminPanelEnableInventory("guilds").show(player);
            }
            if(event.getSlot() == 11) {
                new AdminPanelEnableInventory("kits").show(player);
            }
            if(event.getSlot() == 12) {
                new AdminPanelEnableInventory("case").show(player);
            }
            if(event.getSlot() == 13) {
                new AdminPanelEnableInventory("diamond_items").show(player);
            }
        });
    }
}
