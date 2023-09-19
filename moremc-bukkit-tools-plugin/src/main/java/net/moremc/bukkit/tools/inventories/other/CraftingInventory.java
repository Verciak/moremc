package net.moremc.bukkit.tools.inventories.other;

import net.moremc.bukkit.tools.data.Crafting;
import net.moremc.bukkit.tools.service.CraftingService;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CraftingInventory extends InventoryHelperImpl
{
    private final CraftingService service;
    private final int page;

    public CraftingInventory(int page, CraftingService service) {
        super("&dCraftingi " + " &f" + page + "&8/&f" +  service.getCraftings().size(), 45);
        this.service = service;
        this.page = page;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassSlots = new Integer[] {0, 1, 3, 5, 7, 8, 9, 17, 27, 35, 36, 37, 39, 41, 43, 44};

        Arrays.stream(yellowGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));

        inventory.setItem(25, new ItemBulider(Material.WORKBENCH)
                .setName("&aNacisnij aby wykraftować.")
                .toItemStack()
        );
        inventory.setItem(38, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&cPoprzednia strona")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=")
                .toItemStack()
        );
        inventory.setItem(42, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&aNastępna strona")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=") .toItemStack()
        );
        inventory.setItem(40, new ItemBulider(Material.DARK_OAK_FENCE_GATE).setName("&cWyjście").toItemStack());

        Optional<Crafting> item = service.find(page);
        if(!item.isPresent()) {
            player.closeInventory();
            return;
        }
        inventory.setItem(4, item.get().getResultItem());
        item.get().getRequiredItems().forEach(inventory::setItem);

        onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 40){
                player.closeInventory();
            }

            if(event.getSlot() == 25) {
                List<ItemStack> items = new ArrayList<>();
                item.get().getRequiredItems().forEach((integer, itemStack) -> { items.add(itemStack); });

                if(!ItemUtilities.hasItems(player, items)) {
                    player.closeInventory();
                    player.sendMessage(MessageHelper.translateText("&cNie posiadasz potrzebnych przedmiotów!"));
                    return;
                }
                items.forEach(itemStack -> player.getInventory().removeItem(itemStack));

                player.closeInventory();
                player.getInventory().addItem(item.get().getResultItem());
            }
            if(event.getSlot() == 38) {
                if(page == 1) {
                    player.sendMessage(MessageHelper.colored("&cNie znaleziono poprzedniej strony!"));
                    return;
                }
                new CraftingInventory(page - 1, service).show(player);
            }
            if(event.getSlot() == 42) {
                if(page == service.getCraftings().size()) {
                    player.sendMessage(MessageHelper.colored("&cNie znaleziono następnej strony!"));
                    return;
                }
                new CraftingInventory(page + 1, service).show(player);
            }
        });
    }
}
