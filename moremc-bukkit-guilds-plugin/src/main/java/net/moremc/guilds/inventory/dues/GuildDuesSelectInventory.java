package net.moremc.guilds.inventory.dues;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.guild.dues.GuildDues;
import net.moremc.api.entity.guild.dues.category.GuildDuesCategory;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.utilities.SignEditorUtilities;
import net.moremc.bukkit.tools.utilities.ItemUtilities;

import java.text.DecimalFormat;
import java.util.Arrays;

public class GuildDuesSelectInventory extends InventoryHelperImpl {


    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildDuesSelectInventory() {
        super("&dWybierz składke", 45);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        Integer[] glassPurpleSlots = new Integer[]{0, 1, 2, 6, 7, 8, 9, 17, 39, 40, 41};
        Integer[] glassMagmaSlots = new Integer[]{3, 4, 5, 18, 26, 27, 35, 36, 37, 38, 42, 43, 44};
        Integer[] glassGraySlots = new Integer[]{11, 13, 15, 19, 21, 23, 25, 29, 31, 33};
        Integer[] glassBlackSlots = new Integer[]{10, 12, 14, 16, 28, 30, 32, 34};

        Arrays.stream(glassPurpleSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack()));
        Arrays.stream(glassMagmaSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(glassGraySlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));
        Arrays.stream(glassBlackSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack()));


        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            for (GuildDues guildDues : guild.getDuesList()) {
                ItemHelper duesItem = new ItemHelper(Material.MINECART)
                        .setName(" &fSkladka &d#" + guildDues.getId())
                        .addLore("")
                        .addLore("                 &5&lWAZNE!!!")
                        .addLore(" &fCo należy zrobić gdy ta składka sie zakończyla?")
                        .addLore(" &fjeśli informacje poniżej są juz &cniedostępne")
                        .addLore(" &fa wczesniej była składka której jeszcze nie odebrałeś")
                        .addLore(" &fmożesz nadal to zrobić wystarczy kliknąć &d&lQ")
                        .addLore(" &fdziała to do momentu gdy nie odpalisz następnej składki")
                        .addLore("")
                        .addLore(" &fCzy wczesniej była odpalona składka: " + (guildDues.getCategory() == GuildDuesCategory.END ? "&aTAK" : "&cNIE"))
                        .addLore("")
                        .addLore(" &fNazwa: &d" + (!guildDues.hasActive() ? "&cbrak" : "#1"))
                        .addLore(" &fAktywna: &d" + (!guildDues.hasActive() ? "&cNIE" : "&aTAK"))
                        .addLore(" &fOrganizator: &d" + (!guildDues.hasActive() ? "&cbrak" : guildDues.getOwner()))
                        .addLore(" &fCel do końca: " + (!guildDues.hasActive() ? "&cbrak"
                                : "&8(&d" + guildDues.getAmountWithdraw() + "&8/&5" + guildDues.getAmountEnd() + "&7, &5" + new DecimalFormat("##.##").format((((guildDues.getAmountWithdraw()) * 1.0) / guildDues.getAmountEnd()) * 100) + "%&8)"))
                        .addLore((!guildDues.hasActive() ?
                                "&fSkładka gotowa do &aaktywacji" :
                                " &fWymagane: &d" + guildDues.getMaterialName()))
                        .addLore("")
                        .addLore("&5Q &8- &fAby odebrać składke")
                        .addLore("&5Lewy &8- &fAby przejść dalej.")
                        .addLore("&5Prawy &8- &fAby wplacić przedmioty");
                inventory.setItem(guildDues.getInventorySlot(), duesItem.toItemStack());
            }

            this.onClick(player, event -> {
                event.setCancelled(true);


                guild.findGuildDuesByInventorySlot(event.getSlot()).ifPresent(duesGuildFind -> {

                    guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {
                        if (!guildPlayer.hasPermission(guild, 30)) {
                            if (!duesGuildFind.hasActive()) {
                                player.closeInventory();
                                player.sendMessage(MessageHelper.colored("&5&lSkładki &8>> &fTa składka jest &cwyłączona!!"));
                                return;
                            }
                            SignEditorUtilities.openSignEditorToPlayer(player, "", "PODAJ ILOŚĆ", "#" + duesGuildFind.getId(), "&8-----");
                            return;
                        }

                        if (event.getClick() == ClickType.DROP) {
                            if (duesGuildFind.getCategory() != GuildDuesCategory.END) {
                                player.closeInventory();
                                player.sendMessage(MessageHelper.colored("&5&lSkładki &8>> &fTa składka jeszcze nie jest gotowa!!"));
                                return;
                            }
                            player.closeInventory();
                            player.sendMessage(MessageHelper.colored("&5&lSkładka &8>> &fOdebrałeś pomyślnie składkę gildyjną&8: &5&l#&d&l" + duesGuildFind.getId()));
                            ItemUtilities.addItem(player, new ItemHelper(Material.getMaterial(duesGuildFind.getMaterialName().toUpperCase()), duesGuildFind.getAmountWithdraw()));
                            duesGuildFind.setCategory(guild, GuildDuesCategory.WAITING);
                            duesGuildFind.setActive(guild, false);
                            duesGuildFind.setAmountWithdraw(guild, 0);
                            return;
                        }
                        if (event.getClick() == ClickType.RIGHT) {
                            if (!duesGuildFind.hasActive()) {
                                player.closeInventory();
                                player.sendMessage(MessageHelper.colored("&5&lSkładki &8>> &fTa składka jest &cwyłączona!!"));
                                return;
                            }
                            SignEditorUtilities.openSignEditorToPlayer(player, "", "PODAJ ILOŚĆ", "#" + duesGuildFind.getId(), "&8-----");
                            return;
                        }
                        if (event.getClick() == ClickType.LEFT) {
                            GuildDuesInventory guildDuesInventory = new GuildDuesInventory(duesGuildFind);
                            guildDuesInventory.show(player);
                        }
                    });
                });
            });
        });
    }
}
