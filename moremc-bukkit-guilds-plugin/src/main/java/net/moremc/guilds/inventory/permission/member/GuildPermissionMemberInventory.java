package net.moremc.guilds.inventory.permission.member;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.guild.player.GuildPlayer;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.guilds.inventory.permission.member.settings.GuildPermissionMemberSettingsInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GuildPermissionMemberInventory extends InventoryHelperImpl {


    private final GuildService guildService = API.getInstance().getGuildService();
    private final int indexPage;
    private final String guildName;

    public GuildPermissionMemberInventory(int indexPage, String guildName) {
        super("&dUprawnienia członków", 54);
        this.indexPage = indexPage;
        this.guildName = guildName;
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {


            this.onClick(player, event -> {
                event.setCancelled(true);

                switch (event.getSlot()){
                    case 48: {
                        GuildPermissionMemberInventory previousPageInventory = new GuildPermissionMemberInventory(this.indexPage - 1, this.guildName);
                        if (!previousPageInventory.show(player)) {
                            player.sendMessage(MessageHelper.colored("&cBrak poprzedniej strony w gildii!"));
                            return;
                        }
                        previousPageInventory.show(player);
                        break;
                    }

                    case 50: {
                        GuildPermissionMemberInventory previousPageInventory = new GuildPermissionMemberInventory(this.indexPage + 1, this.guildName);
                        if (!previousPageInventory.show(player)) {
                            player.sendMessage(MessageHelper.colored("&cBrak nastepnej strony w gildii!"));
                            return;
                        }
                        previousPageInventory.show(player);
                        break;
                    }
                }

                ItemStack itemStack = event.getCurrentItem();
                if(itemStack == null)return;
                if(!ItemIdentityHelper.compareIdentity(itemStack, "nickName"))return;
                String key = ItemIdentityHelper.getItemIdentityTagName(itemStack, "nickName");


                guild.findGuildPlayerByNickname(key).ifPresent(guildPlayer -> {
                    GuildPermissionMemberSettingsInventory guildPermissionMemberSettingsInventory = new GuildPermissionMemberSettingsInventory(0, key);
                    guildPermissionMemberSettingsInventory.show(player);
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

        this.setBackPageItem(inventory,48);
        this.setNextPageItem(inventory,50);


        AtomicBoolean status = new AtomicBoolean(false);
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            status.set(this.initialize(inventory, guild, player.getName()));
        });
        return status.get();
    }
    private boolean initialize(Inventory inventory, GuildImpl guild, String nickName) {

        List<GuildPlayer> guildPlayerList = new ArrayList<>(guild.getPlayerMap().values());
        int fromIndex = indexPage * 28;

        if(fromIndex < 0)return false;
        if (guildPlayerList.size() <= fromIndex) {
            return !(indexPage > 0);
        }
        List<GuildPlayer> paginatedGuildPlayerList = guildPlayerList.subList(fromIndex, Math.min(fromIndex + 28, guildPlayerList.size()));
        if (paginatedGuildPlayerList.isEmpty()) {
            return false;
        }
        for (GuildPlayer guildPlayer : paginatedGuildPlayerList) {

            ItemHelper guildPlayerItem = new ItemHelper(Material.SKULL_ITEM,1, (short) 3)
                    .setIdentifyItem("nickName", guildPlayer.getNickName())
                    .setOwner(guildPlayer.getNickName())
                    .setName("&a" + guildPlayer.getNickName())
                    .addLore("")
                    .addLore(" &fRanga w gildii&8: &a" + guildPlayer.getPlayerType().name())
                    .addLore("&dLewy &8- &fAby przejść do zarządzania uprawnieniami.");

            inventory.addItem(guildPlayerItem.toItemStack());
        }
        return true;
    }
}
