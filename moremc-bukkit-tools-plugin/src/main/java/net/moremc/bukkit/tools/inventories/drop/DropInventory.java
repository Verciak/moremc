package net.moremc.bukkit.tools.inventories.drop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class DropInventory extends InventoryHelperImpl {


    private final DropStoneInventory stoneInventory = new DropStoneInventory();
    private final DropChestInventory chestInventory = new DropChestInventory();
    private final DropCobblexInventory cobblexInventory = new DropCobblexInventory();

    public DropInventory() {
        super("&dDropy serwera", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());


        inventory.setItem(4, new ItemHelper(Material.BARRIER)
                .setName(" ").toItemStack());


        ItemHelper dropItem = new ItemHelper(Material.MOSSY_COBBLESTONE)
                .setName("&dDrop z cobblex")
                .addLore("")
                .addLore("&dLewy &8- &fAby przejść dalej.");

        inventory.setItem(20, dropItem.toItemStack());

        dropItem = new ItemHelper(Material.STONE)
                .setName("&dDrop z kamienia")
                .addLore("")
                .addLore("&dLewy &8- &fAby przejść dalej.");

        inventory.setItem(21, dropItem.toItemStack());

        dropItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&dDrop z case")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmYwMDk5MWFlNzljYWMzNGUzNTgzNTdjMTNkYjY5Y2E2ZmVkODJiOGE1OWE5MjE4OWYzZmU0ZmRiMmU0ZTU4OSJ9fX0=")
                .addLore("")
                .addLore("&dLewy &8- &fAby przejść dalej.");

        inventory.setItem(23, dropItem.toItemStack());

        dropItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&dDrop z bossa")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYTE5Y2NmODQ4ZTgwNDFiYjhiODBlMGE1ZDhiZmQ3ZmYiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU1Y2UxZGIwMDcwNmMwMDA3NzUzMDNiNzhlOWRkZDQ0NTMyMzM3NWMzNmEyZGZmNDNlNTAwMDMzMjA5NDQ3NiIsInByb2ZpbGVJZCI6IjM4OGY4MDUxNTVmOTQ2NTU5MjE0NTQ4Njk0OTM4ZGEyIiwidGV4dHVyZUlkIjoiMjU1Y2UxZGIwMDcwNmMwMDA3NzUzMDNiNzhlOWRkZDQ0NTMyMzM3NWMzNmEyZGZmNDNlNTAwMDMzMjA5NDQ3NiJ9fSwic2tpbiI6eyJpZCI6ImExOWNjZjg0OGU4MDQxYmI4YjgwZTBhNWQ4YmZkN2ZmIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI1NWNlMWRiMDA3MDZjMDAwNzc1MzAzYjc4ZTlkZGQ0NDUzMjMzNzVjMzZhMmRmZjQzZTUwMDAzMzIwOTQ0NzYiLCJwcm9maWxlSWQiOiIzODhmODA1MTU1Zjk0NjU1OTIxNDU0ODY5NDkzOGRhMiIsInRleHR1cmVJZCI6IjI1NWNlMWRiMDA3MDZjMDAwNzc1MzAzYjc4ZTlkZGQ0NDUzMjMzNzVjMzZhMmRmZjQzZTUwMDAzMzIwOTQ0NzYifSwiY2FwZSI6bnVsbH0=")
                .addLore("")
                .addLore("&dLewy &8- &fAby przejść dalej.");

        inventory.setItem(24, dropItem.toItemStack());

        dropItem = new ItemHelper(Material.DRAGON_EGG)
                .setName("&dDrop z pandory")
                .addLore("")
                .addLore("&dLewy &8- &fAby przejść dalej.");

        inventory.setItem(31, dropItem.toItemStack());




        this.onClick(player, event -> {
            event.setCancelled(true);


            switch (event.getSlot()){
                case 21:{
                    this.stoneInventory.show(player);
                    break;
                }
                case 23:{
                    this.chestInventory.show(player);
                    break;
                }
                case 49:{
                    player.closeInventory();
                    break;
                }
                case 20:{
                    this.cobblexInventory.show(player);
                    break;
                }
            }

        });


    }
}
