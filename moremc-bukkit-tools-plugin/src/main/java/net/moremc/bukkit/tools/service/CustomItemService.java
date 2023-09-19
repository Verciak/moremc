package net.moremc.bukkit.tools.service;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.bukkit.api.bulider.ItemBulider;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CustomItemService
{
    private final Map<String, ItemStack> items = new ConcurrentHashMap<>();

    public void load() {
        items.put("cobblex", new ItemBulider(Material.MOSSY_COBBLESTONE, 1).visibleFlag()
                .setName("&d&lCOBBLEX")
                .setLore(Arrays.asList("",
                        "&8>> &fPostaw na ziemii klikajac &dPPM",
                        "&8>> &fa staniesz sie bogatszy...")
                )
                .addEnchant(Enchantment.ARROW_DAMAGE, 10).toItemStack()
        );
        items.put("case", new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).visibleFlag()
                .setName("&d&lMAGICZNA SKRZYNIA")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQwNmU0NTMxOGU5YTRhNmJmZTEzMmYyMDJmZTNjZWFjMTVkMTFlYWVkYmVmMWViMDZhMzc2ZGI0MzMwOTBhOCJ9fX0=")
                .setIdentifyItem("case", "case")
                .setLore(Arrays.asList(
                        " &8Oj dzieki temu naprawde mozesz byc bogaty",
                        "",
                        "&8>> &fPostaw na ziemii klikajac &dPPM",
                        "&8>> &fa staniesz sie bogatszy...",
                        "&8>> &fWiecej informacji pod: &d/drop")
                ).toItemStack()
        );
        items.put("backpack-shears", new ItemBulider(Material.SHEARS)
                .setName("&d&lNożyce do plecka")
                .setIdentifyItem("backpack-shears", "shears")
                .setLore(Arrays.asList(
                        "",
                        "  &7Krótki opis jak tego użyć?",
                        "&8>> &fOPIS #1",
                        "&8>> &fOPIS #2")
                ).toItemStack()
        );
        items.put("candy", new ItemBulider(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&d&lCukierek")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmViNGRlNzQyMzAxYjNiZDAwY2Y4YmU5ZjZkYzM2OWY1YTZjZWM5ZTExODk0NDQzMmM1NWIyNzhhZmUzOTZhZSJ9fX0=")
                .toItemStack()
        );
        items.put("axe_guild_sand", new ItemBulider(Material.GOLD_AXE).visibleFlag()
                .setName("&8* &5Zaznacz teren &8*")
                .setIdentifyItem("axe_select", GuildGeneratorType.SAND.name())
                .setLore(Arrays.asList(
                        "",
                        "  &7Krótki opis jak to działa?",
                        "&8>> &fZaznacz &5PPM &fwybrany blok.",
                        "&8>> &fNastępnie zaznacz &5LPM &fwybrany blok.",
                        "",
                        "&8>> &fOd wybranych miejsc w &dgóre&7/&ddół&8, &dlewo&7/&dprawo",
                        "            &fzacznie się generować &dPiasek.",
                        "&8>> &fMusisz jedynie wrócić do panelu i &duruchomić &fgenerator.")
                ).toItemStack());

        items.put("axe_guild_obsidian", new ItemBulider(Material.GOLD_AXE).visibleFlag()
                .setName("&8* &5Zaznacz teren &8*")
                .setIdentifyItem("axe_select", GuildGeneratorType.OBSIDIAN.name())
                .setLore(Arrays.asList(
                        "",
                        "  &7Krótki opis jak to działa?",
                        "&8>> &fZaznacz &dPPM &fwybrany blok.",
                        "&8>> &fNastępnie zaznacz &dLPM &fwybrany blok.",
                        "",
                        "&8>> &fOd wybranych miejsc w &dgóre&7/&ddół&8, &dlewo&7/&dprawo",
                        "            &fzacznie się generować &5Obsydian.",
                        "&8>> &fMusisz jedynie wrócić do panelu i &duruchomić &fgenerator.")
                ).toItemStack());

        items.put("axe_guild_air", new ItemBulider(Material.GOLD_AXE).visibleFlag()
                .setName("&8* &5Zaznacz teren &8*")
                .setIdentifyItem("axe_select", GuildGeneratorType.AIR.name())
                .setLore(Arrays.asList(
                        "",
                        "  &7Krótki opis jak to działa?",
                        "&8>> &fZaznacz &dPPM &fwybrany blok.",
                        "&8>> &fNastępnie zaznacz &dLPM &fwybrany blok.",
                        "",
                        "&8>> &fOd wybranych miejsc w &dgóre&7/&ddół&8, &dlewo&7/&dprawo",
                        "            &fbloki zaczną znikać.",
                        "&8>> &fMusisz jedynie wrócić do panelu i &duruchomić &fgenerator.")
                ).toItemStack());

        items.put("stonework", new ItemBulider(Material.ENDER_STONE).visibleFlag()
                .setName("&dStowniarka")
                .setLore(Arrays.asList(
                        "",
                        "  &7Krótki opis jak to działa?",
                        "&fPostaw ją na ziemii a zacznie generować",
                        "&fsię tobie kamień na dwie kratki do góry.")
                ).toItemStack());

        items.put("golden_apple", new ItemBulider(Material.GOLDEN_APPLE).toItemStack());
    }
    public ItemStack find(String item) {
        return items.get(item);
    }

    public Map<String, ItemStack> getItems() {
        return items;
    }
}
