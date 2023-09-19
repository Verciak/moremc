package net.moremc.guilds.inventory;

import net.moremc.guilds.utilities.GuildRegenerationUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.guild.regeneration.GuildRegenerationType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GuildRegenerationInventory extends InventoryHelperImpl {

    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildRegenerationInventory() {
        super("&dRegeneracja gildii", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {


            Integer[] whiteSlots = new Integer[]{0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53};
            Integer[] magentaSlots = new Integer[]{2, 6, 10, 16, 18, 26, 27, 35, 47, 51};
            Integer[] purpleSlots = new Integer[]{3, 5, 11, 15, 38, 42, 48, 49, 50};
            Integer[] blackSlots = new Integer[]{19, 25, 43, 37};

            Arrays.stream(whiteSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 0).setName(" ").toItemStack()));
            Arrays.stream(magentaSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
            Arrays.stream(purpleSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack()));
            Arrays.stream(blackSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack()));

            ItemHelper[] itemStacks = new ItemHelper[]{new ItemHelper(Material.OBSIDIAN), new ItemHelper(Material.STONE), new ItemHelper(Material.SAND),
                    new ItemHelper(Material.GLASS), new ItemHelper(Material.CHEST), new ItemHelper(Material.WOOL), new ItemHelper(Material.BOOKSHELF),
                    new ItemHelper(Material.ENCHANTMENT_TABLE), new ItemHelper(Material.ANVIL), new ItemHelper(Material.ENDER_STONE), new ItemHelper(Material.HOPPER),
                    new ItemHelper(Material.DISPENSER), new ItemHelper(Material.getMaterial(356)), new ItemHelper(Material.REDSTONE_BLOCK),
                    new ItemHelper(Material.REDSTONE_LAMP_OFF), new ItemHelper(Material.REDSTONE), new ItemHelper(Material.WOOD_DOOR),
                    new ItemHelper(Material.PISTON_STICKY_BASE), new ItemHelper(Material.STONE_BUTTON)};


            for (ItemHelper itemStack : itemStacks) {
                itemStack.visibleFlag().addEnchant(Enchantment.ARROW_DAMAGE, 10)
                        .addLore("")
                        .addLore("       &7Tutaj możesz ustawić jakie bloki")
                        .addLore("  &7mają zostać pominięte podczas regenerowania")
                        .addLore("")
                        .addLore("          &5&lINFO")
                        .addLore("&8>> &fStatus: &aAktywny&8(&fBędzie regenerowany&8)")
                        .addLore("&8>> &fID: &d" + itemStack.toItemStack().getTypeId())
                        .addLore("")
                        .addLore("          &5&lPRZEŁĄCZANIE")
                        .addLore("&dLewy &8- &fAby &awłączyć &fregenerowanie")
                        .addLore("&dLewy &8- &fAby &cwyłączyć &fregenerowanie");

                inventory.addItem(itemStack.toItemStack());
            }

            int blocks = 0;
            int x = guild.getRegeneration().getBlockStateList().size();

            long delta = Math.abs(blocks - x);
            long time = TimeUnit.SECONDS.toMillis(delta);

            ItemHelper generateItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzNjA0NTIwOGY5YjVkZGNmOGM0NDMzZTQyNGIxY2ExN2I5NGY2Yjk2MjAyZmIxZTUyNzBlZThkNTM4ODFiMSJ9fX0=")
                    .setName("&aZacznij regenerowanie")
                    .addLore("")
                    .addLore("     &5&lINFORMACJE")
                    .addLore(" &fZniszczone bloki: &d" + guild.getRegeneration().getBlockStateList().size())
                    .addLore(" &fCzas na wygenerowanie: &d" + DataHelper.getCringeTimeToString(System.currentTimeMillis() - time))
                    .addLore(" &fKoszt całkowity regeneracji: &a" + (guild.getRegeneration().getBlockStateList().size() / 100 * 10) + " emeraldów")
                    .addLore(" &fTwoja gildia posiada: &a" + guild.getEmeraldsCount() + " emeraldów")
                    .addLore("")
                    .addLore("     &5&lJAK AKTYWOWAĆ?")
                    .addLore("&dLewy &8- &fAby rozpocząć prace.");

            inventory.setItem(48, generateItem.toItemStack());

            inventory.setItem(50, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setName("&cWstrzymaj prace").setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=").toItemStack());



            this.onClick(player, event -> {
                event.setCancelled(true);

                switch (event.getSlot()) {
                    case 48: {
                        if(guild.findGuildGeneratorIsActive().isPresent()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cJakiś generator jest uruchomiony."));
                            return;
                        }
                        if(guild.getRegeneration().getBlockStateList().size() <= 0){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cGildia nie potrzebuje rengeracji."));
                            return;
                        }
                        
                        if (guild.getRegeneration().getRegenerationType().equals(GuildRegenerationType.START)) {
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&aTwoja gildia jest już regenerowana."));
                            return;
                        }
                        player.closeInventory();
                        GuildRegenerationUtilities.startRegeneration(guild.getName());
                        break;
                    }
                    case 50:{
                        player.closeInventory();
                        guild.getRegeneration().setRegenerationType(GuildRegenerationType.END);
                        break;
                    }
                }
            });
        });
    }
}
