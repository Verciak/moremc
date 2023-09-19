package net.moremc.bukkit.tools.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.moremc.bukkit.api.helper.ItemHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtilities {

    public static void addItem(Player player, ItemHelper itemHelper){
        if(player.getInventory().firstEmpty() == -1){
            player.getWorld().dropItem(player.getLocation(), itemHelper.toItemStack());
        }else{
            player.getInventory().addItem(itemHelper.toItemStack());
        }
    }
    public static void addItem(Player player, ItemStack itemHelper){
        if(player.getInventory().firstEmpty() == -1){
            player.getWorld().dropItem(player.getLocation(), itemHelper);
        }else{
            player.getInventory().addItem(itemHelper);
        }
    }
    public static void remove(ItemStack is, Player player, int amount) {
        int removed = 0;
        boolean all = false;
        List<ItemStack> toRemove = new ArrayList<>();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; ++i) {
            ItemStack item = contents[i];
            if (item != null && !item.getType().equals(Material.AIR) && item.getType().equals(is.getType()) && item.getDurability() == is.getDurability() && !all && removed != amount) {
                if (item.getAmount() == amount) {
                    if (removed == 0) {
                        toRemove.add(item.clone());
                        all = true;
                        removed = item.getAmount();
                    }
                    else {
                        int a = amount - removed;
                        ItemStack s = item.clone();
                        s.setAmount(a);
                        toRemove.add(s);
                        removed += a;
                        all = true;
                    }
                }
                else if (item.getAmount() > amount) {
                    if (removed == 0) {
                        ItemStack s2 = item.clone();
                        s2.setAmount(amount);
                        toRemove.add(s2);
                        all = true;
                        removed = amount;
                    }
                    else {
                        int a = amount - removed;
                        ItemStack s = item.clone();
                        s.setAmount(a);
                        toRemove.add(s);
                        removed += a;
                        all = true;
                    }
                }
                else if (item.getAmount() < amount) {
                    if (removed == 0) {
                        toRemove.add(item.clone());
                        removed = item.getAmount();
                    }
                    else {
                        int a = amount - removed;
                        if (a == item.getAmount()) {
                            toRemove.add(item.clone());
                            removed += item.getAmount();
                            all = true;
                        }
                        else if (item.getAmount() > a) {
                            ItemStack s = item.clone();
                            s.setAmount(a);
                            toRemove.add(s);
                            removed += a;
                            all = true;
                        }
                        else if (item.getAmount() < a) {
                            toRemove.add(item.clone());
                            removed += item.getAmount();
                        }
                    }
                }
            }
        }
        removeItem(player, toRemove);
    }
    public static void removeItem(Player player, List<ItemStack> items) {
        if (player == null || items == null || items.isEmpty()) {
            return;
        }
        for (ItemStack is : items) {
            player.getInventory().removeItem(is);
        }
    }
    public static int getAmountOf(Player p, Material m, Short d) {
        int sum = 0;
        int bound = p.getInventory().getSize();
        for (int i = 0; i < bound; i++) {
            ItemStack s = p.getInventory().getItem(i);
            if (s != null && s.getType().equals(m) && s.getDurability() == d) {
                int amount = s.getAmount();
                sum += amount;
            }
        }
        return sum;
    }
    public static int getAmountOfCustom(Player p, ItemHelper itemHelper) {
        int sum = 0;
        int bound = p.getInventory().getSize();
        for (int i = 0; i < bound; i++) {
            ItemStack s = p.getInventory().getItem(i);
            if (s != null && s.getType().equals(itemHelper.toItemStack().getType()) && s.getDurability() == itemHelper.toItemStack().getDurability() && s.getItemMeta() != null && s.getItemMeta().getDisplayName() != null && s.getItemMeta().getDisplayName().equalsIgnoreCase(itemHelper.toItemStack().getItemMeta().getDisplayName())) {
                int amount = s.getAmount();
                sum += amount;
            }
        }
        return sum;
    }
    public static int getCountItemInInventory(Player player, ItemHelper itemHelper) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(itemStack -> itemStack != null && itemStack.getType() == itemHelper.toItemStack().getType())
                .mapToInt(ItemStack::getAmount)
                .sum();
    }
    public static int getCountItemInInventory(Player player, ItemStack itemStackContains) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(itemStack -> itemStack != null && itemStack.getType() == itemStackContains.getType() && itemStack.getDurability() == itemStackContains.getDurability())
                .filter(itemStack -> itemStack.getItemMeta().getDisplayName() == null)
                .mapToInt(ItemStack::getAmount)
                .sum();
    }
    public static boolean hasItems(Player player, List<ItemStack> items) {
        for (ItemStack item : items) {
            if (!hasItem(player, item)) return false;
        }
        return true;
    }
    public static boolean hasItem(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;

        return hasItem(player, item, item.getAmount());
    }
    public static boolean hasItem(Player player, ItemStack item, int amount) {
        if (item == null || item.getType() == Material.AIR) return false;
        return player.getInventory().containsAtLeast(item, amount);
    }
    public static boolean hasItem(Player player, ItemHelper itemHelper){
        return Arrays.stream(player.getInventory().getContents())
                .anyMatch(itemStack -> itemStack != null &&
                        !itemStack.getType().equals(Material.AIR) &&
                itemStack.getType().equals(itemHelper.toItemStack().getType()) &&
                        itemStack.getItemMeta() != null &&
                itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(itemHelper.toItemStack().getItemMeta().getDisplayName()));
    }


    public static int getCountItemInEnderchest(Player player, ItemHelper itemHelper) {
        return Arrays.stream(player.getEnderChest().getContents())
                .filter(itemStack -> itemStack != null && itemStack.getType() == itemHelper.toItemStack().getType() && itemStack.getDurability() == itemHelper.toItemStack().getDurability())
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    public static void removeItem(Player player, ItemStack itemStack){
        player.getInventory().removeItem(itemStack);
    }
    public static void removeItem(Player player, ItemHelper itemStack){
        player.getInventory().removeItem(itemStack.toItemStack());
    }

    public static void addItem(Player player, List<ItemStack> drops) {
        for(ItemStack itemStack : drops) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            } else {
                player.getInventory().addItem(itemStack);
            }
        }
    }
}
