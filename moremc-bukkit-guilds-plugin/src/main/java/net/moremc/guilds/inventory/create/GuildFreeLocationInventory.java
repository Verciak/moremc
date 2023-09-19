package net.moremc.guilds.inventory.create;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.cache.InventoryCache;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.sectors.helper.SectorTransferHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuildFreeLocationInventory extends InventoryHelperImpl {

    private final InventoryCache inventoryCache = BukkitAPI.getInstance().getInventoryCache();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final SectorService sectorService = API.getInstance().getSectorService();
    private final UserService userService = API.getInstance().getUserService();
    private final GuildFreeLocationSelectSectorInventory freeLocationSelectSectorInventory = new GuildFreeLocationSelectSectorInventory();

    private final int page;
    private final String sectorName;

    public GuildFreeLocationInventory(int page, String sectorName) {
        super("&dWybierz miejsce dla gildii", 54);
        this.page = page;
        this.sectorName = sectorName;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 26, 27, 35, 47, 51};


        inventory.setItem(4, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY1ZjE1OTg4NmNjNTMxZmZlYTBkOGFhNWY5MmVkNGU1ZGE2NWY3MjRjMDU3MGFmODZhOTBiZjAwYzY3YzQyZSJ9fX0=")
                .setName("&dWybierz wolne miejsce").toItemStack());

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cPowrót").toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);

            switch (event.getSlot()){
                case 48: {
                    GuildFreeLocationInventory previousPageInventory = new GuildFreeLocationInventory(this.page - 1,this.sectorName);
                    if (!previousPageInventory.show(player)) {
                        player.sendMessage(MessageHelper.colored("&cBrak poprzedniej strony w wolnych miejscach!"));
                        return;
                    }
                    previousPageInventory.show(player);
                    break;
                }

                case 50: {
                    GuildFreeLocationInventory previousPageInventory = new GuildFreeLocationInventory(this.page + 1,this.sectorName);
                    if (!previousPageInventory.show(player)) {
                        player.sendMessage(MessageHelper.colored("&cBrak nastepnej strony w wolynch miejscach!"));
                        return;
                    }
                    previousPageInventory.show(player);
                    break;
                }
            }

            if (event.getSlot() == 49) {
                this.freeLocationSelectSectorInventory.show(player);
            }
            if (event.getCurrentItem() == null) return;
            if (!ItemIdentityHelper.compareIdentity(event.getCurrentItem(), "id")) return;
            int key = Integer.parseInt(ItemIdentityHelper.getItemIdentityTagName(event.getCurrentItem(), "id"));
            this.guildService.findGuildAreaById(key).ifPresent(guildArea -> {
                this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                    this.sectorService.findSectorByLocation("world", guildArea.getX(), guildArea.getZ()).ifPresent(sector -> {
                        if (!sector.getInfo().isOnline()) {
                            player.closeInventory();
                            player.sendMessage(MessageHelper.colored("&cTen sektor jest aktualnie offline."));
                            return;
                        }
                        if(!guildArea.isActive()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.translateText("&cKtoś aktualnie przegląda tą pozycje."));
                            return;
                        }
                        user.setViewTerrainGuild(true);
                        user.setViewTerrainGuildTime(System.currentTimeMillis() + TimeType.SECOND.getTime(10));
                        user.setLocationBeforeCreate(new LocationSerializer(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        user.setGuildAreaSelect(guildArea);
                        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, user);
                        guildArea.setActive(false);
                        SectorTransferHelper.saveDataPlayerSector(player, new Location(player.getWorld(), guildArea.getX(), 100, guildArea.getZ()), user, true, sector.getName());
                    });

                });
            });
        });
    }

    @Override
    public boolean tryInitializeInventoryWithResult(Player player, Inventory inventory) {

        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 26, 27, 35, 47, 51};


        inventory.setItem(4, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY1ZjE1OTg4NmNjNTMxZmZlYTBkOGFhNWY5MmVkNGU1ZGE2NWY3MjRjMDU3MGFmODZhOTBiZjAwYzY3YzQyZSJ9fX0=")
                .setName("&dWybierz wolne miejsce").toItemStack());

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cPowrót").toItemStack());

        this.setBackPageItem(inventory, 48);
        this.setNextPageItem(inventory, 50);

        return this.initialize(inventory);
    }
    public boolean initialize(Inventory inventory) {

        List<GuildArea> guildAreaList = new ArrayList<>(this.guildService.getGuildAreaList());
        int fromIndex = page * 28;

        if(fromIndex < 0)return false;

        if (guildAreaList.size() <= fromIndex) {
            return !(page > 0);
        }
        List<GuildArea> paginatedGuildAreaList = guildAreaList.subList(fromIndex, Math.min(fromIndex + 24, guildAreaList.size()));
        if (paginatedGuildAreaList.isEmpty()) {
            return false;
        }
        for (GuildArea guildArea : paginatedGuildAreaList) {
            if (!guildArea.isActive()) continue;

            this.sectorService.findSectorByLocation("world", guildArea.getX(), guildArea.getZ()).ifPresent(sector -> {
                if(!sector.getName().equalsIgnoreCase(this.sectorName))return;

                ItemHelper freePlaceItem = new ItemHelper(Material.PAPER)
                        .setIdentifyItem("id", String.valueOf(guildArea.getId()))
                        .setName(" &2Wolne miejsce do założenia gildii")
                        .addLore("")
                        .addLore(" &fKordynaty &8(&dX&8:&5 " + guildArea.getX() + "&7, &dZ&8:&5 " + guildArea.getZ() + "&8)")
                        .addLore(" &fSektor&8 (&7Nazwa&8:&d " + sector.getName() + "&7, &7Online&8: " + ((sector.getInfo().isOnline()) ? "&aTAK" : "&cNIE") + "&8)")
                        .addLore("")
                        .addLore("&fOpis:")
                        .addLore(" &fZostaniesz przetelportowany na &d10 &fsek")
                        .addLore(" &faby podejrzeć wybrany przez ciebie teren")
                        .addLore("")
                        .addLore(" &fJak podejrzeć teren?")
                        .addLore("&dLewy &8- &fAby podejrzeć teren.");

                inventory.addItem(freePlaceItem.toItemStack());
            });
        }
        return true;
    }
}
