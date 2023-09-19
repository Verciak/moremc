package net.moremc.bukkit.tools.inventories.other;

import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class DepositInventory extends InventoryHelperImpl {


    private final UserService userService = API.getInstance().getUserService();

    public DepositInventory() {
        super("&dTwój depozyt", 45);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 18, 27, 28, 35, 36, 37, 39, 41, 43, 44};
        Integer[] grayGlassSlots = new Integer[]{2, 4, 6, 10, 11, 12, 13, 14, 15, 16, 18, 19, 25, 26, 28, 29, 30, 31, 32, 33, 34, 38, 42};

        Arrays.stream(yellowGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));
        inventory.setItem(4, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            ItemHelper depositItem = new ItemHelper(Material.ARROW, 10)
                    .setName("&aStrzały")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &aTEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &a10 strzał")
                    .addLore("&fPosiadasz w schowku&8: &a" + user.getDepositCount("arrow") + " strzał")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(13, depositItem.toItemStack());


            depositItem = new ItemHelper(Material.GOLDEN_APPLE, 1, (short) 1)
                    .setName("&5Złote jabłka")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &dTEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &d1 &5złote jabłko")
                    .addLore("&fPosiadasz w schowku&8: &5" + user.getDepositCount("enchantment_golden_apple") + " &dzłotych jabłek")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(20, depositItem.toItemStack());

            depositItem = new ItemHelper(Material.GOLDEN_APPLE, 10)
                    .setName("&5Złote jabłka")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &dTEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &d10 &5złotych jabłek")
                    .addLore("&fPosiadasz w schowku&8: &5" + user.getDepositCount("golden_apple") + " &dzłotych jabłek")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(21, depositItem.toItemStack());

            depositItem = new ItemHelper(Material.ENDER_PEARL, 3)
                    .setName("&dPerły endermana")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &5TEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &53 &dperły endermana")
                    .addLore("&fPosiadasz w schowku&8: &5" + user.getDepositCount("ender_pearl") + " &dpereł endermana")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(22, depositItem.toItemStack());

            depositItem = new ItemHelper(Material.EGG, 8)
                    .setName("&7Jajka")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &fTEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &f8 &7jajek")
                    .addLore("&fPosiadasz w schowku&8: &f" + user.getDepositCount("egg") + " &7jajek")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(23, depositItem.toItemStack());

            depositItem = new ItemHelper(Material.SNOW_BALL, 8)
                    .setName("&fSnieżki")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &fTEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &78 &fśnieżek")
                    .addLore("&fPosiadasz w schowku&8: &7" + user.getDepositCount("snowball") + " &fśnieżek")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(24, depositItem.toItemStack());

            depositItem = new ItemHelper(Material.PACKED_ICE, 32)
                    .setName("&3Bloki lodu")
                    .addLore("")
                    .addLore("&7Poniżej znajdziesz informacje dot.")
                    .addLore("          &3TEGO SCHOWKA")
                    .addLore("")
                    .addLore("&fLimit serwerowy&8: &b32 &3bloków lodu")
                    .addLore("&fPosiadasz w schowku&8: &b" + user.getDepositCount("ice_block") + " &bbloków lodu")
                    .addLore("")
                    .addLore("&dLewy &8- &fAby wypłacić do limitu");

            inventory.setItem(31, depositItem.toItemStack());


            ItemHelper withdrawItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQwNzQyMTZiZDA2NTdjMjQxMDA1Y2JjNDBiZDJmOTQzYzBmOGIzMDU3M2NkOWM5MDMzZDgyOWEwMTZmNmIwZiJ9fX0=")
                    .setName("&aWypłać wszystko do limitu")
                    .addLore("")
                    .addLore("&7Nasz system obliczy wszystko")
                    .addLore("&7jeśli będziesz czegoś potrzebować")
                    .addLore("&7zostanie to tobie wypłacone do limitu")
                    .addLore("")
                    .addLore("&aKliknij &8- &fAby wypłacić wszystko");


            inventory.setItem(40, withdrawItem.toItemStack());


            this.onClick(player, event -> {
                event.setCancelled(true);

                switch (event.getSlot()) {
                    case 13: {
                        this.depositSort(player, user, new ItemHelper(Material.ARROW), 10,"arrow");
                        break;
                    }
                    case 20: {
                        this.depositSort(player, user, new ItemHelper(Material.GOLDEN_APPLE, 1, (short) 1), 1,"enchantment_golden_apple");
                        break;
                    }
                    case 21: {
                        this.depositSort(player, user, new ItemHelper(Material.GOLDEN_APPLE), 10,"golden_apple");
                        break;
                    }
                    case 22: {
                        this.depositSort(player, user, new ItemHelper(Material.ENDER_PEARL), 3,"ender_pearl");
                        break;
                    }
                    case 23: {
                        this.depositSort(player, user, new ItemHelper(Material.EGG), 8,"egg");
                        break;
                    }
                    case 24: {
                        this.depositSort(player, user, new ItemHelper(Material.SNOW_BALL), 8,"snowball");
                        break;
                    }
                    case 31: {
                        this.depositSort(player, user, new ItemHelper(Material.PACKED_ICE), 32,"ice_block");
                        break;
                    }
                    case 40: {
                        player.closeInventory();
                        this.depositSort(player, user, new ItemHelper(Material.ARROW), 10,"arrow");
                        this.depositSort(player, user, new ItemHelper(Material.GOLDEN_APPLE, 1, (short) 1), 1,"enchantment_golden_apple");
                        this.depositSort(player, user, new ItemHelper(Material.GOLDEN_APPLE), 10,"golden_apple");
                        this.depositSort(player, user, new ItemHelper(Material.EGG), 8,"egg");
                        this.depositSort(player, user, new ItemHelper(Material.SNOW_BALL), 8,"snowball");
                        this.depositSort(player, user, new ItemHelper(Material.PACKED_ICE), 32,"ice_block");
                        this.depositSort(player, user, new ItemHelper(Material.ENDER_PEARL), 3,"ender_pearl");
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
                        player.sendTitle("", MessageHelper.translateText("&aPomyślnie wypłacono wszystko do limitu."));
                    }
                }


            });
        });
    }

    public void depositSort(Player player, User user, ItemHelper itemHelper, int limit, String patchMap) {
        int amount = ItemUtilities.getCountItemInInventory(player, itemHelper);
        if (user.getDepositCount(patchMap) <= 0) return;
        if (amount >= limit) return;
        amount = limit - amount;
        ItemUtilities.addItem(player, itemHelper.setAmount(amount));
        user.removeDepositItem(patchMap, amount);
    }
}
