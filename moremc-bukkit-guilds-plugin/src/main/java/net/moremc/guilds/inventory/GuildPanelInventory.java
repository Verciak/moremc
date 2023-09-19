package net.moremc.guilds.inventory;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.ArmorStandHelper;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.PlayerCameraHelper;
import net.moremc.bukkit.api.helper.type.SendType;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.utilities.SignEditorUtilities;
import net.moremc.guilds.inventory.dues.GuildDuesSelectInventory;
import net.moremc.guilds.inventory.generator.GuildGeneratorAirInventory;
import net.moremc.guilds.inventory.generator.GuildGeneratorObsidianInventory;
import net.moremc.guilds.inventory.generator.GuildGeneratorSandInventory;
import net.moremc.guilds.inventory.permission.GuildPermissionSelectInventory;
import net.moremc.guilds.inventory.visual.GuildVisualSelectMenuEditorInventory;

import java.text.DecimalFormat;
import java.util.Arrays;

public class GuildPanelInventory extends InventoryHelperImpl {



    private final GuildPermissionSelectInventory permissionSelectInventory = new GuildPermissionSelectInventory();
    private final GuildGuardInventory guardInventory = new GuildGuardInventory();
    private final GuildQuestInventory questInventory = new GuildQuestInventory();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();
    private final GuildDuesSelectInventory guildDuesSelectInventory = new GuildDuesSelectInventory();
    private final GuildVisualSelectMenuEditorInventory guildVisualSelectMenuEditorInventory = new GuildVisualSelectMenuEditorInventory();
    private final GuildRegenerationInventory guildRegenerationInventory = new GuildRegenerationInventory();
    private final GuildTreasureInventory treasureInventory = new GuildTreasureInventory();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    public GuildPanelInventory() {
        super("&dPanel gildyjny", 54);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack());
        }
        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 4, 18, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(4, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());
        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjdż").toItemStack());

        ItemHelper contributionsItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=")
                .setName("&5Składki");

        inventory.setItem(10, contributionsItem.toItemStack());

        ItemHelper regenerationItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyOGNmNmZhYjhhZTdlZmZiNTFjZDNhYzQyZDJlZTI3NTA0ZTQ0MDAyZmE0ZjYyOWE0NTA5MGZhMTY3YTQ3OCJ9fX0=")
                .setName("&fRegeneracja");

        inventory.setItem(12, regenerationItem.toItemStack());

        ItemHelper permissionItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdiMGY4YTJmMGJlMTY5NDkwY2U1OWEzOWJlNGU4YmIxM2YxZjlkMjViMWFkYTc5ODRiNzRlMmZkYjg4YjlhNCJ9fX0=")
                .setName("&aSchematy uprawnień");

        inventory.setItem(14, permissionItem.toItemStack());


        ItemHelper obsidianExponentItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTc5ZDBlOTZjMzUxZTJlMmQ5MDQyYTQ2ODJkZWM0MzBjNGU1MjI1NmVkNzdjZjgwYmQwMjY3ZjdjYWJlMzMwMCJ9fX0=")
                .setName("&5Wykładacz obsydianu");

        inventory.setItem(20, obsidianExponentItem.toItemStack());

        ItemHelper moatDiggerItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlYTIxMzU4Mzg0NjE1MzQzNzJmMmRhNmM4NjJkMjFjZDVmM2QyYzcxMTlmMmJiNjc0YmJkNDI3OTEifX19")
                .setName("&dKopacz fos");

        inventory.setItem(22, moatDiggerItem.toItemStack());

        ItemHelper sandExpanderItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVjMDE2MjFmZWViN2YzZThjYjM4OWJmNDYyZjg1Yjk4MDdkYzlkM2RkMjZlMDEwYjA0MWE1MzA2YzUzNDhkNCJ9fX0=")
                .setName("&dWykładacz piasku");

        inventory.setItem(24, sandExpanderItem.toItemStack());

        ItemHelper treasury = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTgzMmZkYzM4ZTZhOGUwNWVlYzM5NmJhZmIwYTgxZjkzN2E2MTdmMTVhNzUwOWQ4NWRjOTgyZGJmMGFiNWE5ZiJ9fX0=")
                .setName("&5Skarbiec");

        inventory.setItem(29, treasury.toItemStack());

        ItemHelper bossSpawnItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiYTE5Y2NmODQ4ZTgwNDFiYjhiODBlMGE1ZDhiZmQ3ZmYiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU1Y2UxZGIwMDcwNmMwMDA3NzUzMDNiNzhlOWRkZDQ0NTMyMzM3NWMzNmEyZGZmNDNlNTAwMDMzMjA5NDQ3NiIsInByb2ZpbGVJZCI6IjM4OGY4MDUxNTVmOTQ2NTU5MjE0NTQ4Njk0OTM4ZGEyIiwidGV4dHVyZUlkIjoiMjU1Y2UxZGIwMDcwNmMwMDA3NzUzMDNiNzhlOWRkZDQ0NTMyMzM3NWMzNmEyZGZmNDNlNTAwMDMzMjA5NDQ3NiJ9fSwic2tpbiI6eyJpZCI6ImExOWNjZjg0OGU4MDQxYmI4YjgwZTBhNWQ4YmZkN2ZmIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI1NWNlMWRiMDA3MDZjMDAwNzc1MzAzYjc4ZTlkZGQ0NDUzMjMzNzVjMzZhMmRmZjQzZTUwMDAzMzIwOTQ0NzYiLCJwcm9maWxlSWQiOiIzODhmODA1MTU1Zjk0NjU1OTIxNDU0ODY5NDkzOGRhMiIsInRleHR1cmVJZCI6IjI1NWNlMWRiMDA3MDZjMDAwNzc1MzAzYjc4ZTlkZGQ0NDUzMjMzNzVjMzZhMmRmZjQzZTUwMDAzMzIwOTQ0NzYifSwiY2FwZSI6bnVsbH0=")
                .setName("&5Spawn bossa");

        inventory.setItem(33, bossSpawnItem.toItemStack());

        ItemHelper changeGuildTypeItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4MDk3MmI3YzMyZGE2NTBjYzg2ZTlhYWQ2NjVlZDhkMGQ0OGEzYmRiMGJhMjM0ZDRmY2VhYzAyNGM3OTUyZSJ9fX0=")
                .setName("&dZmiana rodzaju gildi");

        inventory.setItem(34, changeGuildTypeItem.toItemStack());


        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            if(guild.getLocation() != null) {
                ItemHelper magnificationItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                        .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmZkNzA0Y2ZhYWI0OTc0NmMxZDIxMjdjNzAxZTEzYjQ2MWE0MzQ0YTdjOGY4OGNhMGVhMzNhNDBmZjRhOWVmNCJ9fX0=")
                        .setName("&fPowiekszenie terenu gildyjnego")
                        .addLore("")
                        .addLore("&8Dzięki tej opcji możesz zwiększyć teren gildi")
                        .addLore("")
                        .addLore("           &d&lPOZIOMY")
                        .addLore("&8>> &fPoziom &nI &aOdblokowany&8(&f16&dx&f64&8)")
                        .addLore("&8>> &fPoziom &nII " + (guild.getLocation().getSize() > 16 ? "&aOdblokowany" : "&cZablokowany") + "&8(&f32&dx&f64&8)")
                        .addLore("&8>> &fPoziom &nIII " + (guild.getLocation().getSize() > 32 ? "&aOdblokowany" : "&cZablokowany") + "&8(&f48&dx&f64&8)")
                        .addLore("&8>> &fPoziom &nIV " + (guild.getLocation().getSize() > 48 ? "&aOdblokowany" : "&cZablokowany") + "&8(&f64&dx&f64&8)")
                        .addLore("")
                        .addLore((guild.getLocation().getSize() >= 64 ? "      &a&lODBLOKOWANO WSZYSTKIE POZIOMY" : ""))
                        .addLore("")
                        .addLore("           &d&lINFORMACJA")
                        .addLore("&8>> &fAktualny teren gildi: &8(&d" + guild.getLocation().getSize() + "&7x&564&7, &d" + new DecimalFormat("##.##").format((((guild.getLocation().getSize()) * 1.0) / 64) * 100) + "%&8)")
                        .addLore("&8>> &fTwoja gildia posiada: &a" + guild.getEmeraldsCount() + " emeraldów")
                        .addLore("    &fKoszt: &a128 emeraldów")
                        .addLore("")
                        .addLore("&dLewy &8- &fAby zakupić te ulepszenie");

                inventory.setItem(16, magnificationItem.toItemStack());
            }


            ItemHelper emeraldDepositItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNmYmIyZDY3YjZmM2UxZWExNTg0NTlhMTFhNWE4N2Q0NDkyMTc0YzJlYTA4ODEyMmRlMGVkOWQzNzllMDdjZiJ9fX0=")
                    .setName("&aWpłać emeraldy")
                    .addLore("")
                    .addLore("&8>> &fAktualnie jest&8: &a" + guild.getEmeraldsCount());

            inventory.setItem(28, emeraldDepositItem.toItemStack());
        });


        ItemHelper periscopeItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA2ZWJmMDMwYWMwMGNmNjljYTc2Y2JmZGVhNzExMWVjYzY1Yzg5NjU0MmM5Nzc4NjQ1OGEzOWFlNzYzYTRmYSJ9fX0=")
                .setName("&dPeryskop");

        inventory.setItem(38, periscopeItem.toItemStack());

        ItemHelper quardItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRkZDc0ZGZjYzQ0N2JjMmU3ODdkNWJhMTczNjFmMjdkOTEwZGVjYWZkMjc1ZDYwYmQyYWVhZjgzMjcwIn19fQ==")
                .setName("&aOchrona gildi");

        inventory.setItem(40, quardItem.toItemStack());

        ItemHelper questItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY1ODk4YWQ1NGU2MzRjNTlmYWI1YjI4NGQ0OWIzZTI1ZDAxNTUxMmNhYTNhYjU2MjBjZWNmMDBiODRmMTM0NSJ9fX0=")
                .setName("&bQuesty gildie");

        inventory.setItem(42, questItem.toItemStack());

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            this.onClick(player, event -> {
                event.setCancelled(true);

                if(event.getSlot() == 38) {
                    if (!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())) {
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.colored("&cAkcja dostępna tylko na terenie gildii."));
                        return;
                    }
                    this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                        this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {
                            user.setPeriscope(true);

                            Location location = new Location(player.getWorld(), guild.getLocation().getX(), 100, guild.getLocation().getZ(), 82, 82);
                            ArmorStandHelper armorStandHelper = bukkitUser.getArmorStandHelperList().get(4);
                            armorStandHelper.setDisplayName(Arrays.asList(""));
                            armorStandHelper.setLocation(location);
                            armorStandHelper.send(SendType.UPDATE, location, "null");

                            EntityArmorStand entityArmorStand = (EntityArmorStand) armorStandHelper.getEntity().get(0);
                            entityArmorStand.setGravity(false);
                            PlayerCameraHelper.setCamera(player, entityArmorStand);
                        });
                    });
                }
                if(event.getSlot() == 16){
                    if(guild.getLocation().getSize() >= 64){
                        player.closeInventory();
                        player.sendTitle(MessageHelper.colored("&d&lGILDIA"), MessageHelper.colored("&8>> &fTwoja gildia osiagnela maksymlny rozmiar!"));
                        return;
                    }
                    if(guild.getEmeraldsCount() < 128){
                        player.closeInventory();
                        player.sendTitle(MessageHelper.colored("&d&lGILIDA"), MessageHelper.colored("&8>> &fTwoja gildia nie posiada tylu emeraldow!"));
                        return;
                    }

                    guild.setEmeraldsCount(guild.getEmeraldsCount() - 128);
                    guild.addSize(16);
                    this.show(player);
                    guild.sendMessage("&d&lGILIDA &8>> &fTeren gildi zostal zwiekszony przez: &d" + player.getName() + "\n&8>> &fAktualny teren gildi: &8(&d" + guild.getLocation().getSize() + "&7x&564&7, &d" +  new DecimalFormat("##.##").format((((guild.getLocation().getSize()) * 1.0) / 64) * 100) + "%&8)");
                    guild.synchronize(SynchronizeType.UPDATE);
                    return;
                }

                if(event.getSlot() == 29){
                    guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {

                        if(!guildPlayer.hasPermission(guild, 34)){
                            player.closeInventory();
                            player.sendMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej akcji poproś kogoś w gildii."));
                            return;
                        }
                        if(guild.getTreasure().isOpen()){
                            player.closeInventory();
                            player.sendTitle("", MessageHelper.colored("&cAktualnie ktoś przebywa w skarbcu gildyjnym."));
                            return;
                        }
                        this.treasureInventory.show(player);
                    });
                }

                if(event.getSlot() == 12){
                    if(!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.colored("&cAkcja dostępna tylko na terenie gildii."));
                        return;
                    }
                    this.guildRegenerationInventory.show(player);
                }

                if(event.getSlot() == 34){
                    if(!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.colored("&cAkcja dostępna tylko na terenie gildii."));
                        return;
                    }
                    this.guildVisualSelectMenuEditorInventory.show(player);
                }

                if(event.getSlot() == 28){
                    SignEditorUtilities.openSignEditorToPlayer(player, "", "&fPodaj ilość", "&aEmeraldów", "&8&m-------");
                }

                if(event.getSlot() == 10){
                    this.guildDuesSelectInventory.show(player);
                }

                if (event.getSlot() == 14) {
                    this.permissionSelectInventory.show(player);
                }
                if (event.getSlot() == 40) {
                    this.guardInventory.show(player);
                }
                if (event.getSlot() == 42) {
                    this.questInventory.show(player);
                }
                if (event.getSlot() == 24) {
                    if(!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.colored("&cAkcja dostępna tylko na terenie gildii."));
                        return;
                    }
                    guild.findGuildGeneratorByType(GuildGeneratorType.SAND).ifPresent(guildGenerator -> {
                        new GuildGeneratorSandInventory(guildGenerator).show(player);
                    });
                }
                if(event.getSlot() == 22){
                    if(!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.colored("&cAkcja dostępna tylko na terenie gildii."));
                        return;
                    }
                    guild.findGuildGeneratorByType(GuildGeneratorType.AIR).ifPresent(guildGenerator -> {
                        new GuildGeneratorAirInventory(guildGenerator).show(player);
                    });
                }
                if(event.getSlot() == 20){
                    if(!guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.colored("&cAkcja dostępna tylko na terenie gildii."));
                        return;
                    }
                    guild.findGuildGeneratorByType(GuildGeneratorType.OBSIDIAN).ifPresent(guildGenerator -> {
                        new GuildGeneratorObsidianInventory(guildGenerator).show(player);
                    });
                }
            });
        });
    }
}
