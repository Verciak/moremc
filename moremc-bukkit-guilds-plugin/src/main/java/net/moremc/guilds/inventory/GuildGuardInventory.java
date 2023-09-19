package net.moremc.guilds.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class GuildGuardInventory extends InventoryHelperImpl
{

    public GuildGuardInventory() {
        super("&dOchrona gildi", 54);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        ItemHelper quardItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRkZDc0ZGZjYzQ0N2JjMmU3ODdkNWJhMTczNjFmMjdkOTEwZGVjYWZkMjc1ZDYwYmQyYWVhZjgzMjcwIn19fQ==")
                .setName("&dOchrona gildi");

        inventory.setItem(4, quardItem.toItemStack());

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());

        ItemHelper tntItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU0MzUyNjgwZDBiYjI5YjkxMzhhZjc4MzMwMWEzOTFiMzQwOTBjYjQ5NDFkNTJjMDg3Y2E3M2M4MDM2Y2I1MSJ9fX0=")
                .setName("&aPrzerywa wybuch tnt")
                .setLore(Arrays.asList(
                        "",
                        "&8>> &fCena: &a120 bloków emeraldów",
                        "&8>> &fDziałanie: &aPo zakupie przerywa działanie tnt na 32s"
                ));

        inventory.setItem(21, tntItem.toItemStack());

        ItemHelper armyItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRkZDc0ZGZjYzQ0N2JjMmU3ODdkNWJhMTczNjFmMjdkOTEwZGVjYWZkMjc1ZDYwYmQyYWVhZjgzMjcwIn19fQ==")
                .setName("&dTworzy dodtkowa armie")
                .setLore(Arrays.asList(
                        "",
                        "&8>> &fCena: &a150 bloków emeraldów",
                        "&8>> &fDziałanie: &aPo zakupie tworzy sie fala zombie,",
                        " &aktóra bronia twojej gildi przez 25s"
                ));

        inventory.setItem(22, armyItem.toItemStack());

        ItemHelper weaknessItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTZmMmU5OGM1NzJlNGFhZjY1OTU4NTY3YjJlMzEzNmMyNDU4YTIwNmQwMjcwNTQzNWYyOTM0OGIwNDI1MjQyOCJ9fX0=")
                .setName("&8Daje efekt osłabienia")
                .setLore(Arrays.asList(
                        "",
                        "&8>> &fCena: &a200 bloków emeraldów",
                        "&8>> &fDziałanie: &aPo zakupie twoi wrogowie dostaja,",
                        " &aefekt osłabienia na 25s"
                ));

        inventory.setItem(23, weaknessItem.toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);
        });
    }
}
