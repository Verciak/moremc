package net.moremc.guilds.inventory.permission.group.settings;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.data.guild.GuildPermissionDataArray;
import net.moremc.api.data.guild.type.GuildPermissionActionDataType;
import net.moremc.api.data.guild.type.GuildPermissionDataMaterialType;
import net.moremc.api.entity.guild.permission.GuildPermission;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GuildPermissionGroupSettingsInventory extends InventoryHelperImpl {

    private final GuildPermissionData permissionData = CommunicatorPlugin.getInstance().getPermissionData();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final String groupName;
    private final int indexPage;

    public GuildPermissionGroupSettingsInventory(int indexPage, String groupName) {
        super("&dUprawnienia &8(&f" + groupName + "&8)", 54);

        this.indexPage = indexPage;
        this.groupName = groupName;
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        this.onClick(player, event -> {
            event.setCancelled(true);


            switch (event.getSlot()){
                case 48: {
                    GuildPermissionGroupSettingsInventory previousPageInventory = new GuildPermissionGroupSettingsInventory(this.indexPage - 1, this.groupName);
                    if (!previousPageInventory.show(player)) {
                        player.sendMessage(MessageHelper.colored("&cBrak poprzedniej strony w gildii!"));
                        return;
                    }
                    previousPageInventory.show(player);
                    break;
                }

                case 50: {
                    GuildPermissionGroupSettingsInventory previousPageInventory = new GuildPermissionGroupSettingsInventory(this.indexPage + 1, this.groupName);
                    if (!previousPageInventory.show(player)) {
                        player.sendMessage(MessageHelper.colored("&cBrak nastepnej strony w gildii!"));
                        return;
                    }
                    previousPageInventory.show(player);
                    break;
                }
            }

            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) return;
            if (!ItemIdentityHelper.compareIdentity(currentItem, "id")) return;
            int key = Integer.parseInt(ItemIdentityHelper.getItemIdentityTagName(currentItem, "id"));

            this.permissionData.findDataById(key).ifPresent(permissionData -> {
                this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                    guild.findPermissionByName(this.groupName.split("_")[0]).ifPresent(guildPermission -> {

                        if (!guildPermission.has(permissionData.getId())){
                            guildPermission.setAccess(guild,false, permissionData.getId());
                            this.show(player);
                            return;
                        }
                        guildPermission.setAccess(guild, true, permissionData.getId());
                        show(player);
                    });
                });
            });
        });
    }

    @Override
    public boolean tryInitializeInventoryWithResult(Player player, Inventory inventory) {


        Integer[] yellowGlassPaneSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 36, 44, 45, 46, 48, 50, 52, 53};
        Integer[] grayGlassPaneSlots = new Integer[]{2, 6, 18, 4, 26, 27, 35, 47, 51};

        Arrays.stream(yellowGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassPaneSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE,1, (short) 7).setName(" ").toItemStack()));

        inventory.setItem(49, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());
        inventory.setItem(4, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());

        this.setBackPageItem(inventory, 48);
        this.setNextPageItem(inventory,50);

        AtomicBoolean status = new AtomicBoolean(false);
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            guild.findPermissionByName(this.groupName.split("_")[0]).ifPresent(guildPermission -> {
              status.set(this.initialize(inventory, guildPermission));
            });
        });
        return status.get();
    }
    public boolean initialize(Inventory inventory, GuildPermission guildPermission) {

        List<GuildPermissionDataArray> guildPermissionDataList = Arrays.asList(this.permissionData.getPermissionDataArrays());
        int fromIndex = indexPage * 28;

        if(fromIndex < 0)return false;
        if (guildPermissionDataList.size() <= fromIndex) {
            return !(indexPage > 0);
        }
        List<GuildPermissionDataArray> paginatedGuildPlayerList = guildPermissionDataList.subList(fromIndex, Math.min(fromIndex + 28, guildPermissionDataList.size()));
        if (paginatedGuildPlayerList.isEmpty()) {
            return false;
        }

        for (GuildPermissionDataArray permissionDataArray : paginatedGuildPlayerList) {
            ItemHelper permissionItem = new ItemHelper((permissionDataArray.getMaterialType().equals(GuildPermissionDataMaterialType.HEAD) ? Material.SKULL_ITEM
                    : (permissionDataArray.getMaterialNameOrUrl().equalsIgnoreCase("CRAFTING_TABLE") ? Material.getMaterial(58) : (Material.valueOf(permissionDataArray.getMaterialNameOrUrl())))), (permissionDataArray.getActionType().equals(GuildPermissionActionDataType.PLACE) ? 2 : 1), (short) 3)
                    .setName(permissionDataArray.getPolishName())
                    .setIdentifyItem("id", String.valueOf(permissionDataArray.getId()))
                    .addLore("")
                    .addLore("  &fDostęp&8: " + (guildPermission.has(permissionDataArray.getId()) ? "&c&cNIE &l✘" : "&aTAK &l✔"))
                    .addLore("");

            if (permissionDataArray.getMaterialType().equals(GuildPermissionDataMaterialType.HEAD)) {
                permissionItem.setOwnerUrl(permissionDataArray.getMaterialNameOrUrl());
            }

            if (!guildPermission.has(permissionDataArray.getId()) && !permissionItem.toItemStack().getType().equals(Material.SKULL_ITEM)) {
                permissionItem.visibleFlag().addEnchant(Enchantment.ARROW_FIRE, 10);
            }
            permissionItem.addLore((permissionDataArray.getActionType().equals(GuildPermissionActionDataType.BREAK_PLACE) ? "&dLewy &8- &fAby zabrać/nadać dostęp do niszczenia/stawiania." : "&dLewy &8- &fAby zabrać/nadać dostęp."));
            inventory.addItem(permissionItem.toItemStack());
        }
        return true;
    }
}
