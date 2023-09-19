package net.moremc.guilds.inventory.permission;

import net.moremc.guilds.inventory.permission.group.GuildPermissionGroupInventory;
import net.moremc.guilds.inventory.permission.member.GuildPermissionMemberInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.cache.InventoryCache;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class GuildPermissionSelectInventory extends InventoryHelperImpl {

    private final GuildPermissionGroupInventory groupInventory = new GuildPermissionGroupInventory();
    private final InventoryCache inventoryCache = BukkitAPI.getInstance().getInventoryCache();
    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildPermissionSelectInventory() {
        super("&dWybierz typ konfiguracji", 54);
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


        ItemHelper memberItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&dUprawnienia członków")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEyMmE1MDNkN2E2ZjU3ODAyYjAzYWY3NjI0MTk0YTRjNGY1MDc3YTk5YWUyMWRkMjc2Y2U3ZGI4OGJjMzhhZSJ9fX0=");

        inventory.setItem(20, memberItem.toItemStack());

        ItemHelper groupItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&dUprawnienia dla grup")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEyMmE1MDNkN2E2ZjU3ODAyYjAzYWY3NjI0MTk0YTRjNGY1MDc3YTk5YWUyMWRkMjc2Y2U3ZGI4OGJjMzhhZSJ9fX0=");


        inventory.setItem(24, groupItem.toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 20) {
                this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                    GuildPermissionMemberInventory guildPermissionMemberInventory = new GuildPermissionMemberInventory(0, guild.getName());
                    guildPermissionMemberInventory.show(player);
                });
                return;
            }
            if(event.getSlot() == 24){
                this.groupInventory.show(player);
            }

        });
    }
}
