package net.moremc.guilds.inventory.dues;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.data.guild.dues.DuesGuildData;
import net.moremc.api.data.guild.dues.DuesGuildDataArray;
import net.moremc.api.entity.guild.dues.GuildDues;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.utilities.SignEditorUtilities;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.text.DecimalFormat;
import java.util.Arrays;

public class GuildDuesInventory extends InventoryHelperImpl {


    private final DuesGuildData duesGuildData = CommunicatorPlugin.getInstance().getDuesGuildData();
    private final GuildDues duesGuild;

    public GuildDuesInventory(GuildDues guildDues) {
        super("&dSkładka &f" + guildDues.getId(), 54);
        this.duesGuild = guildDues;
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        for(int i = 0; i < 6 * 9; i++){
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15)
                    .setName(" ").toItemStack());
        }

        Integer[] integerSlots = new Integer[]{0, 1, 7, 8, 9, 17, 36,44,45, 46,52,53};

        Arrays.stream(integerSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2)
                .setName(" ").toItemStack()));

        ItemHelper duesItem = new ItemHelper(Material.MINECART)
                .setName(this.getInventoryName())
                .addLore("")
                .addLore(" &fNazwa: &d" + (!duesGuild.hasActive() ? "&cbrak" : "#1"))
                .addLore(" &fAktywna: &d" + (!duesGuild.hasActive() ? "&cNIE" : "&aTAK"))
                .addLore(" &fOrganizator: &d" + (!duesGuild.hasActive() ? "&cbrak" : duesGuild.getOwner()))
                .addLore(" &fCel do końca: " + (!duesGuild.hasActive() ? "&cbrak"
                        : "&8(&d" + duesGuild.getAmountWithdraw() + "&8/&5" + duesGuild.getAmountEnd() + "&7, &5" + new DecimalFormat("##.##").format((((duesGuild.getAmountWithdraw()) * 1.0) / duesGuild.getAmountEnd()) * 100) + "%&8)"))
                .addLore((!duesGuild.hasActive() ?
                        " &fSkładka gotowa do &aaktywacji" :
                        " &fWymagane: &d" + duesGuild.getMaterialName()))
                .addLore("");

        inventory.setItem(4, duesItem.toItemStack());


        for (DuesGuildDataArray duesGuildData : this.duesGuildData.getDuesGuildData()) {
            ItemHelper itemDues = new ItemHelper(Material.valueOf(duesGuildData.getMaterialName().toUpperCase()))
                    .setName(duesGuildData.getInventoryName())
                    .setLore(duesGuildData.getInventoryLore());


            inventory.setItem(duesGuildData.getInventorySlot(), itemDues.toItemStack());
        }


        this.onClick(player, event -> {
            event.setCancelled(true);

            DuesGuildDataArray duesGuildData = this.duesGuildData.findDuesGuildByInventorySlot(event.getSlot());
            if (duesGuildData == null) return;
            if (duesGuild.hasActive()) {
                player.closeInventory();
                player.sendMessage(MessageHelper.colored("&5&lSkładki &8>> &fAkcja nie została przetworzona składka już jest &aaktywna!"));
                return;
            }
            SignEditorUtilities.openSignEditorToPlayer(player, "", "WPISZ ILOŚĆ", duesGuildData.getName(),  "" + duesGuild.getId());
        });
    }
}