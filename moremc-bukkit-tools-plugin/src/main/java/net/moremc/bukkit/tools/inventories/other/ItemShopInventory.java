package net.moremc.bukkit.tools.inventories.other;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;

import java.util.Arrays;
import java.util.Optional;

public class ItemShopInventory extends InventoryHelperImpl
{
    private final UserService service = API.getInstance().getUserService();

    public ItemShopInventory() {
        super("&dPrzedmioty z ItemShop'u", 54);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        Optional<User> user = service.findUserByUUID(player.getUniqueId());
        if(!user.isPresent()) {
            return;
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(4, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());
        inventory.setContents(ItemSerializer.encodeItem(user.get().getItemShopItems()));
        inventory.setItem(49, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).setName("&aOdbierz przedmioty").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=").toItemStack());

        onClick(player, event -> {
            event.setCancelled(true);
            if(event.getSlot() == 49) {
                if(user.get().getItemShopItems().equalsIgnoreCase("null")) {
                    player.closeInventory();
                    player.sendMessage(MessageHelper.colored("&cNie masz nic do wypłacenia!"));
                    return;
                }
                ItemStack[] items = ItemSerializer.encodeItem(user.get().getItemShopItems());

                player.getInventory().addItem(items);
                player.closeInventory();

                user.get().setItemShopItems(null);

                player.sendTitle("", MessageHelper.colored("&fPomyślnie &dwypłaciłeś &fprzedmioty z itemshop'u!"));
            }
        });
    }
}
