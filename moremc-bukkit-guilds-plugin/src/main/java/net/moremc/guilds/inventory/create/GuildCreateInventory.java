package net.moremc.guilds.inventory.create;

import net.moremc.api.API;
import net.moremc.api.entity.guild.heart.type.GuildHeartLookType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Arrays;

public class GuildCreateInventory extends InventoryHelperImpl {


    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();

    public GuildCreateInventory() {
        super("&dWybierz wygląd centrum", 45);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack());
        }
        Integer[] yellowGlassSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 18, 27, 28, 35, 36, 37, 39, 41, 43, 44};
        Integer[] grayGlassSlots = new Integer[]{2, 4, 6, 10, 11, 12, 13, 14, 15, 16, 18, 19, 25, 26, 28, 29, 30, 31, 32, 33, 34, 38, 42};

        Arrays.stream(yellowGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE,1, (short) 7).setName(" ").toItemStack()));
        inventory.setItem(4, new ItemHelper(Material.OBSIDIAN).setName("&5Wygląd").toItemStack());
        inventory.setItem(40, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());


        ItemHelper lookItem = new ItemHelper(Material.OBSIDIAN)
                .setIdentifyItem("schematic", GuildHeartLookType.OBSIDIAN.getSchematicName())
                .setName("&7Standardowy")
                .addLore("")
                .addLore("&fNacisnij &dPPM &faby ustawić te centrum gildii.");


        inventory.setItem(20, lookItem.toItemStack());

         lookItem = new ItemHelper(Material.NETHER_STAR)
                 .setIdentifyItem("schematic", GuildHeartLookType.NETHER.getSchematicName())
                 .setName("&cPiekielny")
                 .addLore("")
                 .addLore("&fNacisnij &dPPM &faby ustawić te centrum gildii.");

        inventory.setItem(21, lookItem.toItemStack());

        lookItem = new ItemHelper(Material.ENDER_PORTAL_FRAME)
                .setIdentifyItem("schematic", GuildHeartLookType.ENDER_STONE.getSchematicName())
                .setName("&5Endowy")
                .addLore("")
                .addLore("&fNacisnij &dPPM &faby ustawić te centrum gildii.");

        inventory.setItem(22, lookItem.toItemStack());

        lookItem = new ItemHelper(Material.PUMPKIN)
                .setIdentifyItem("schematic", GuildHeartLookType.PUMPKIN.getSchematicName())
                .setName("&dHalloween")
                .addLore("")
                .addLore("&fNacisnij &dPPM &faby ustawić te centrum gildii.");

        inventory.setItem(23, lookItem.toItemStack());

        lookItem = new ItemHelper(Material.SAND)
                .setIdentifyItem("schematic", GuildHeartLookType.SAND.getSchematicName())
                .setName("&dPiaskowy")
                .addLore("")
                .addLore("&fNacisnij &dPPM &faby ustawić te centrum gildii.");

        inventory.setItem(24, lookItem.toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);


            if(event.getCurrentItem() == null)return;
            if (!ItemIdentityHelper.compareIdentity(event.getCurrentItem(), "schematic")) return;
            String key = ItemIdentityHelper.getItemIdentityTagName(event.getCurrentItem(), "schematic");

            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                    BukkitAPI.getInstance().getSchematicFactoryMap().get(key).pasteGuildSchematic(new Location(Bukkit.getWorld("world"),
                            guild.getLocation().getX(), 38, guild.getLocation().getZ()));

                    player.teleport(new Location(Bukkit.getWorld("world"),
                            guild.getLocation().getX(), 39, guild.getLocation().getZ()));
                });
                user.setGuildAreaSelect(null);
                user.setGuildCreated(false);
                this.userService.synchronizeUser(SynchronizeType.UPDATE, user);
            });
            player.closeInventory();
            player.sendTitle(MessageHelper.colored("&5&lGILDIA"), MessageHelper.colored("&aKonfiguracja przebiegła pomyślnie."));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
        });
        this.onClose(player, event -> {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                user.setGuildAreaSelect(null);
                user.setGuildCreated(false);

                this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                    BukkitAPI.getInstance().getSchematicFactoryMap().get("obsidian").pasteGuildSchematic(new Location(Bukkit.getWorld("world"),
                            guild.getLocation().getX(), 38, guild.getLocation().getZ()));

                    player.teleport(new Location(Bukkit.getWorld("world"),
                            guild.getLocation().getX(), 39, guild.getLocation().getZ()));

                    this.userService.synchronizeUser(SynchronizeType.UPDATE, user);
                });
            });
        });

    }
}
