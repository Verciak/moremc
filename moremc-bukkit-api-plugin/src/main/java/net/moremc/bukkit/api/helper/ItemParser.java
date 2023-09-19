package net.moremc.bukkit.api.helper;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemParser {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[:+',;.]");

    public static ItemHelper parseItem(String input) {
        String[] inputSplitted = input.split(" +");
        String[] itemStackSplitted = inputSplitted[0].split(":");
        ItemHelper builder = new ItemHelper(new ItemStack(Material.matchMaterial(itemStackSplitted[0]),
                        inputSplitted.length > 1 ? Integer.parseInt(inputSplitted[1]) : 1,
                        itemStackSplitted.length > 1 ? Short.parseShort(itemStackSplitted[1]) : 0));

        for (int i = 2; i < inputSplitted.length; i++) {
            String[] itemMetaSplitted = SPLIT_PATTERN.split(inputSplitted[i], 2);
            if (itemMetaSplitted.length < 1) {
                continue;
            }

            Enchantment enchantment = ItemStackEnchantmentHelper.find(itemMetaSplitted[0]);
            if (itemMetaSplitted.length > 1 && itemMetaSplitted[0].equalsIgnoreCase("name")) {
                String displayName = itemMetaSplitted[1].replace('_', ' ');
                builder.setName(displayName);
            } else if (itemMetaSplitted.length > 1 && itemMetaSplitted[0].equalsIgnoreCase("lore")) {
                List<String> lore = Arrays.stream(itemMetaSplitted[1].split("(?<!\\\\)\\|"))
                        .map(line -> line.replace('_', ' ').replace("\\|", "|")).collect(Collectors.toList());
                builder.setLore(lore);
            } else if (enchantment != null) {
                if(builder.toItemStack().getType().equals(Material.ENCHANTED_BOOK)){
                    builder.addEnchantBook(enchantment,
                            itemMetaSplitted.length > 1 ? Integer.parseInt(itemMetaSplitted[1]) : 1);
                    continue;
                }
                builder.addEnchant(enchantment,
                        itemMetaSplitted.length > 1 ? Integer.parseInt(itemMetaSplitted[1]) : 1);
            }
        }

        return builder;
    }
}
