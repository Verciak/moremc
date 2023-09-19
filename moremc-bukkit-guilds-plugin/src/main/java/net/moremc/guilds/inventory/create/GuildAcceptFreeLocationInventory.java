package net.moremc.guilds.inventory.create;

import net.moremc.api.API;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.guild.GuildSchematicPastePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.guilds.GuildsPlugin;
import net.moremc.sectors.helper.SectorTransferHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class GuildAcceptFreeLocationInventory extends InventoryHelperImpl {


    private final GuildService guildService = API.getInstance().getGuildService();
    private final SectorService sectorService = API.getInstance().getSectorService();
    private final UserService userService = API.getInstance().getUserService();

    public GuildAcceptFreeLocationInventory() {
        super("&dPotwierdż zajęcie terenu", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {


        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cPowrót").toItemStack());



        ItemHelper noItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3).setName("&cNIE")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=");


        ItemHelper yesItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3).setName("&aTAK")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=");


        inventory.setItem(20, yesItem.toItemStack());
        inventory.setItem(21, yesItem.toItemStack());
        inventory.setItem(29, yesItem.toItemStack());
        inventory.setItem(30, yesItem.toItemStack());

        inventory.setItem(23, noItem.toItemStack());
        inventory.setItem(24, noItem.toItemStack());
        inventory.setItem(32, noItem.toItemStack());
        inventory.setItem(33, noItem.toItemStack());

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            this.sectorService.findSectorByLocation("world", user.getGuildAreaSelect().getX(), user.getGuildAreaSelect().getZ()).ifPresent(sector -> {
                inventory.setItem(4, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                        .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY1ZjE1OTg4NmNjNTMxZmZlYTBkOGFhNWY5MmVkNGU1ZGE2NWY3MjRjMDU3MGFmODZhOTBiZjAwYzY3YzQyZSJ9fX0=")
                        .setName(" &dPotwierdż zajęcie terenu:")
                        .addLore("")
                        .addLore(" &fX: &d" + user.getGuildAreaSelect().getX())
                        .addLore(" &fZ: &d" + user.getGuildAreaSelect().getZ())
                        .addLore("")
                        .addLore(" &fSektor 8(&7Nazwa&8:&d " + sector.getName() + "&7, &7Online&8: " + ((sector.getInfo().isOnline()) ? "&aTAK" : "&cNIE") + "&8)")
                        .addLore("")
                        .toItemStack());

                this.onClick(player, event -> {
                    event.setCancelled(true);

                    this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                        switch (event.getSlot()) {
                            case 24:
                            case 32:
                            case 33:
                            case 23:{
                                player.closeInventory();
                                this.guildService.findGuildAreaById(user.getGuildAreaSelect().getId()).ifPresent(guildArea -> {
                                    guildArea.setActive(true);
                                });
                                user.setGuildAreaSelect(null);
                                player.sendTitle(MessageHelper.translateText("&c&lGILDIA"), MessageHelper.translateText("&cKontynuuj póżniej&8: &4/g konfiguracja"));
                                this.userService.synchronizeUser(SynchronizeType.UPDATE, user);
                                break;
                            }
                            case 21:
                            case 20:
                            case 29:
                            case 30: {
                                guild.setLocation(new LocationSerializer("world", user.getGuildAreaSelect().getX(), 38, user.getGuildAreaSelect().getZ(), 32));
                                guild.setLocationHome(new LocationSerializer("world", user.getGuildAreaSelect().getX(), 38, user.getGuildAreaSelect().getZ(), 32));
                                guild.synchronize(SynchronizeType.UPDATE);

                                player.closeInventory();
                                player.sendTitle(MessageHelper.translateText(""), MessageHelper.translateText("&aZaraz zostaniesz przeniesiony..."));

                                player.sendTitle(MessageHelper.translateText("&aGILDIA"), MessageHelper.translateText("&aZmień sektor i patrz na scoreboard."));
                                Bukkit.getScheduler().runTaskLaterAsynchronously(GuildsPlugin.getInstance(), () -> {
                                    this.guildService.findGuildAreaById(user.getGuildAreaSelect().getId()).ifPresent(guildArea -> {
                                        this.guildService.getGuildAreaList().remove(guildArea.getId());
                                    });
                                    user.setGuildCreated(true);
                                    user.setViewTerrainGuild(false);
                                    user.setSelectAreaGuildTime(System.currentTimeMillis() + TimeType.MINUTE.getTime(30));
                                    this.userService.synchronizeUser(SynchronizeType.UPDATE, user);
                                    player.getInventory().addItem(GuildsPlugin.getInstance().getCustomItemService().find("guild_create"));
                                    SectorTransferHelper.saveDataPlayerSector(player, new Location(player.getWorld(), user.getGuildAreaSelect().getX(), 34, user.getGuildAreaSelect().getZ()), user, true, sector.getName());
                                    API.getInstance().getNatsMessengerAPI().sendPacket(sector.getName(), new GuildSchematicPastePacket(guild.getName(), user.getGuildAreaSelect().getX(), user.getGuildAreaSelect().getZ()));
                                    }, 50L);
                                break;
                            }
                        }
                    });
                });
            });
        });
    }
}
