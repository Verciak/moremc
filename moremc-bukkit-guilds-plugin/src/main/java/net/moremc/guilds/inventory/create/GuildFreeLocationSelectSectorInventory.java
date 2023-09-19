package net.moremc.guilds.inventory.create;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.inventory.cache.InventoryCache;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.text.DecimalFormat;
import java.util.Arrays;

public class GuildFreeLocationSelectSectorInventory extends InventoryHelperImpl {


    private final SectorService sectorService = API.getInstance().getSectorService();
    private final InventoryCache inventoryCache = BukkitAPI.getInstance().getInventoryCache();

    public GuildFreeLocationSelectSectorInventory() {
        super("&dWybierz sektor dla gildii", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 26, 27, 35, 47, 51};

        inventory.setItem(4, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY1ZjE1OTg4NmNjNTMxZmZlYTBkOGFhNWY5MmVkNGU1ZGE2NWY3MjRjMDU3MGFmODZhOTBiZjAwYzY3YzQyZSJ9fX0=")
                .setName("&dWybierz sektor dla gildii").toItemStack());

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cZakończ póżniej").toItemStack());


        for (Sector sector : this.sectorService.getMap().values()) {
            if(sector.isSpawn() || sector.isTeleport() || sector.isCustom())continue;

            ItemHelper sectorItem = new ItemHelper(Material.PAPER)
                    .setIdentifyItem("sectorName", sector.getName())
                    .setName((sector.getInfo().isOnline() ? "&a" : "&c") + sector.getName())
                    .addLore("")
                    .addLore(" &fGraczy&8: &d" + sector.getInfo().getPlayerList().size() + " &5online")
                    .addLore(" &fTPS&8: &d" + new DecimalFormat("##.##").format(sector.getInfo().getTicksPerSeconds()))
                    .addLore(" &fOstatnia odpowiedż &d" + DataHelper.getTimeToString(sector.getInfo().getLatestInformation()))
                    .addLore("")
                    .addLore("&dLewy &8- &fAby się przejść dalej");

            inventory.addItem(sectorItem.toItemStack());
        }

        this.onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 49){
                player.closeInventory();
            }

            ItemStack itemStack = event.getCurrentItem();
            if(itemStack == null)return;

            if(!ItemIdentityHelper.compareIdentity(itemStack, "sectorName"))return;
            String key = ItemIdentityHelper.getItemIdentityTagName(itemStack, "sectorName");
            GuildFreeLocationInventory guildFreeLocationInventory = new GuildFreeLocationInventory(0, key);
            guildFreeLocationInventory.show(player);
        });

    }
}
