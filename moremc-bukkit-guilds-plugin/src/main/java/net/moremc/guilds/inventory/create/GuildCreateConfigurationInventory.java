package net.moremc.guilds.inventory.create;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class GuildCreateConfigurationInventory extends InventoryHelperImpl {

    private final GuildFreeLocationSelectSectorInventory freeLocationSelectSectorInventory = new GuildFreeLocationSelectSectorInventory();

    public GuildCreateConfigurationInventory() {
        super("&dKonfiguracja gildii", 54);
    }
    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());

        inventory.setItem(4, new ItemHelper(Material.PAPER)
                .setName("&aKonfiguracja gildii").toItemStack());

        ItemHelper selectTerrainItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY1ZjE1OTg4NmNjNTMxZmZlYTBkOGFhNWY5MmVkNGU1ZGE2NWY3MjRjMDU3MGFmODZhOTBiZjAwYzY3YzQyZSJ9fX0=")
                .setName("&2Zajmij teren gildii")
                .addLore("")
                .addLore("&5Lewy &8- &fAby przejść dalej.");

        inventory.setItem(22, selectTerrainItem.toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 22){
                this.freeLocationSelectSectorInventory.show(player);
            }
        });

    }
}
