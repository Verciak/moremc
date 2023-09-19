package net.moremc.guilds.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class GuildQuestInventory extends InventoryHelperImpl
{

    public GuildQuestInventory() {
        super("&dQuesty gildi", 54);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        ItemHelper questItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY1ODk4YWQ1NGU2MzRjNTlmYWI1YjI4NGQ0OWIzZTI1ZDAxNTUxMmNhYTNhYjU2MjBjZWNmMDBiODRmMTM0NSJ9fX0=")
                .setName("&bQuesty gildie");

        inventory.setItem(4, questItem.toItemStack());

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());


        ItemHelper exempleQuestItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY1ODk4YWQ1NGU2MzRjNTlmYWI1YjI4NGQ0OWIzZTI1ZDAxNTUxMmNhYTNhYjU2MjBjZWNmMDBiODRmMTM0NSJ9fX0=")
                .setName("&bQuesty #69");

        inventory.setItem(19, exempleQuestItem.toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);
        });
    }
}
