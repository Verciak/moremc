package net.moremc.bukkit.tools.inventories.sector;

import net.moremc.bukkit.api.helper.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.bulider.ItemBulider;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SectorsInventory extends InventoryHelperImpl
{

    private final SectorService service = API.getInstance().getSectorService();

    public SectorsInventory() {
        super("&dSektory", 45);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 18, 27, 28, 35, 36, 37, 39, 41, 43, 44};
        Integer[] grayGlassSlots = new Integer[]{2, 4, 6, 10, 11, 12, 13, 14, 15, 16, 18, 19, 25, 26, 28, 29, 30, 31, 32, 33, 34, 38, 42};

        Arrays.stream(yellowGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE,1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));
        inventory.setItem(40, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjście").toItemStack());



        service.getMap().values().forEach(sector -> {
            ItemBulider channelItem = new ItemBulider(Material.SKULL_ITEM,1, (short) 3)
                    .setName("&dSektor: &f" + sector.getName())
                    .setOwnerUrl((sector.getInfo().isOnline() ?
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=" :
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0="))
                    .setLore(Arrays.asList(
                            "",
                            " &fGraczy: " + (sector.getInfo().isOnline() ? "&d" + sector.getInfo().getPlayerList().size() : "&cBrak danych"),
                            " &fTPS: " + (sector.getInfo().isOnline() ? "&d" + new DecimalFormat("##.##").format(sector.getInfo().getTicksPerSeconds()) : "&cBrak danych"),
                            " &fOstatnia aktualizacja danych: " + (sector.getInfo().isOnline() ? "&d" + DataHelper.getTimeToString(sector.getInfo().getLatestInformation()) + " &ftemu." : "&cBrak danych"),
                            ""
                    ));

            inventory.addItem(channelItem.toItemStack());
        });
        onClick(player, event -> {
            if(event.getSlot() == 40){
                player.closeInventory();
            }
            event.setCancelled(true);
        });
    }
}
