package net.moremc.bukkit.tools.inventories.other;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;
import java.util.Optional;

public class AwardInventory extends InventoryHelperImpl
{

    private final UserService userService = API.getInstance().getUserService();

    public AwardInventory() {
        super("&dNagrody", 54);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        Optional<User> user = userService.findUserByUUID(player.getUniqueId());
        if(!user.isPresent()) {
            return;
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());

        inventory.setItem(20, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM1OWU1YzdiMDczOGI1NzlmM2I0NDRjMTNhNDdiZWQ0OTZiMzA4MzhiMmVlMmIxMjdjYzU5Y2Q3OThhZWU3NyJ9fX0=")
                .setName("&9Discord")
                .setLore(Arrays.asList(
                        "",
                        "&8>> &fOdebrano: " + (user.get().isDiscord() ? "&aTAK" : "&cNIE"),
                        "&8>> &fNagroda: ",
                        "",
                        "&fInstrukcja odboirou: ",
                        " &fAby odebrac nagrode, wystarczy wpisać swoj nick na kanale nagroda"))
                .toItemStack());

        inventory.setItem(22, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyZTk0NTFjZGIxOTZiNzgxOTVhOGYwYTRiOWMxYzBhMDRmNTgyNzg4NzkyN2I2YTgyYWFkMzljYWIyZjQzMCJ9fX0=")
                .setName("&2Dzienna nagroda")
                .setLore(Arrays.asList(
                        "",
                        "&8>> &fOdebrano: ",
                        "&8>> &fNagroda: ",
                        "",
                        "&fInstrukcja odboirou: ",
                        " "))
                .toItemStack());

        inventory.setItem(24, new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhhN2Q5ZjZkYzljMjQzMzRlOTJkYmJkMTE3MWM3MDk4ZDM4M2VjNGRhOTU3NjhlNjhjOWI5YThlY2M2YjhhYSJ9fX0=")
                .setName("&9Facebook")
                .setLore(Arrays.asList(
                        "",
                        "&8>> &fOdebrano: ",
                        "&8>> &fNagroda: ",
                        "",
                        "&fInstrukcja odboirou: ",
                        " &fAby odebrac nagrode, wystarczy wysłać wiadmość o tresci &d!nagroda twój_nick"))
                .toItemStack());

        onClick(player, event -> {
            event.setCancelled(true);
            switch (event.getSlot()){
                case 49: {
                    player.closeInventory();
                    break;
                }
                case 20: {
                    player.closeInventory();
                    player.sendMessage("chuj ci w dupe weryfikacja");
                    break;
                }
                case 22: {
                    player.sendMessage("Daily Reward");
                    player.closeInventory();
                    break;
                }
                case 24: {
                    player.sendMessage("Facebook");
                    player.closeInventory();
                    break;
                }
            }
        });
    }
}