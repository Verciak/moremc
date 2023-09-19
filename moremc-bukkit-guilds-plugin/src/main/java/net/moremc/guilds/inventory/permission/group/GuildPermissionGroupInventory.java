package net.moremc.guilds.inventory.permission.group;

import net.moremc.guilds.inventory.permission.group.settings.GuildPermissionGroupSelectMemberInventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.guild.permission.GuildPermission;
import net.moremc.api.entity.guild.permission.type.GuildPermissionType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.inventory.cache.InventoryCache;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.utilities.SignEditorUtilities;
import net.moremc.guilds.inventory.permission.group.settings.GuildPermissionGroupSettingsInventory;

import java.util.Arrays;

public class GuildPermissionGroupInventory extends InventoryHelperImpl {


    private final InventoryCache inventoryCache = BukkitAPI.getInstance().getInventoryCache();
    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildPermissionGroupInventory() {
        super("&dWybierz grupe uprawnień", 45);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        Integer[] yellowGlassSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 18, 27, 28, 35, 36, 37, 39, 41, 43, 44};
        Integer[] grayGlassSlots = new Integer[]{2, 4, 6, 10, 11, 12, 13, 14, 15, 16, 18, 26, 28, 29, 30, 31, 32, 33, 34, 38, 42};

        Arrays.stream(yellowGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1 , (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(40, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWyjście").toItemStack());

        inventory.setItem(4, new ItemHelper(Material.ITEM_FRAME)
                .setName(" &aChcesz utworzyć nową grupe?")
                .addLore("")
                .addLore("&5Shift + Lewy &8- &fAby przejść do tworzenia.").toItemStack());

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            for (GuildPermission guildPermission : guild.getPermissionList()) {

                ItemHelper roleItem = new ItemHelper(Material.ITEM_FRAME)
                        .setIdentifyItem("id", String.valueOf(guildPermission.getId()))
                        .setName("&fRola &d" + guildPermission.getName())
                        .addLore("")
                        .addLore("&fDomyślna " + (guildPermission.getType().equals(GuildPermissionType.DEFAULT) ? "&aTAK" : "&cNIE"))
                        .addLore("")
                        .addLore("&5Lewy &8- &fAby przejść do konfiguracji.")
                        .addLore("&5Prawy &8- &fAby ustawić jako domyślną.")
                        .addLore("&5Q &8- &fAby dodać członka do grupy.")
                        .addLore("&5Shift + Lewy &8- &fAby zedytować nazwe grupy.");

                if (guildPermission.getType().equals(GuildPermissionType.DEFAULT)) {
                    roleItem.visibleFlag().addEnchant(Enchantment.ARROW_FIRE, 10);
                }
                inventory.addItem(roleItem.toItemStack());
            }
        });
        this.onClick(player, event -> {
            event.setCancelled(true);


            if(event.getSlot() == 4 && event.getClick() == ClickType.SHIFT_LEFT){
                SignEditorUtilities.openSignEditorToPlayer(player, "",  "-------", "Wpisz nazwe", "^^^");
                return;
            }


            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) return;
            if (!ItemIdentityHelper.compareIdentity(currentItem, "id")) return;
            int key = Integer.parseInt(ItemIdentityHelper.getItemIdentityTagName(currentItem, "id"));


            this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

                guild.findPermissionById(key).ifPresent(guildPermission -> {

                    switch (event.getClick()){
                        case LEFT:{
                            GuildPermissionGroupSettingsInventory guildPermissionGroupSettingsInventory = new GuildPermissionGroupSettingsInventory(0, guildPermission.getName() + "_" + guild.getName());
                            guildPermissionGroupSettingsInventory.show(player);
                            break;
                        }
                        case DROP:{
                            GuildPermissionGroupSelectMemberInventory guildPermissionGroupSelectMemberInventory = new GuildPermissionGroupSelectMemberInventory(0, guildPermission);
                            guildPermissionGroupSelectMemberInventory.show(player);
                            break;
                        }
                        case RIGHT:{
                            for (GuildPermission permission : guild.getPermissionList()) {
                                permission.setType(GuildPermissionType.NORMAL);
                            }
                            guildPermission.setType(GuildPermissionType.DEFAULT);
                            this.show(player);
                            break;
                        }
                        case SHIFT_RIGHT:
                        case SHIFT_LEFT:{
                            SignEditorUtilities.openSignEditorToPlayer(player, "",  "-------", "Podaj nazwe", guildPermission.getName());
                            break;
                        }
                    }
                });
            });
        });
    }
}
