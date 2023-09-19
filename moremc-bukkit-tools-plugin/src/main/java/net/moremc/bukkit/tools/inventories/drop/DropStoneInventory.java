package net.moremc.bukkit.tools.inventories.drop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.data.drop.stone.DropStoneData;
import net.moremc.api.data.drop.stone.DropStoneDataArray;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DropStoneInventory extends InventoryHelperImpl {


    private final DropStoneData stoneDropData = CommunicatorPlugin.getInstance().getDropStoneData();
    private final UserService userService = API.getInstance().getUserService();

    public DropStoneInventory() {
        super("&dDrop z kamienia", 54);
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

        inventory.setItem(49, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cWróć").toItemStack());


        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            for (DropStoneDataArray stoneDataArray : this.stoneDropData.getStoneDataArrays()) {
                try {
                    ItemHelper dropItem = new ItemHelper(Material.valueOf(stoneDataArray.getMaterialName()))
                            .setIdentifyItem("id", String.valueOf(stoneDataArray.getId()))
                            .setName(this.stoneDropData.getInventoryName().replace("{POLISHNAME}", stoneDataArray.getMaterialPolishName()))
                            .setLore(Arrays.stream(this.stoneDropData.getInventoryLore())
                                    .map(s -> {
                                        s = s.replace("{CHANCE}", String.valueOf(stoneDataArray.getChance()));
                                        s = s.replace("{STATUS_FORTUNE}", (stoneDataArray.isFortune() ? "&aTAK &l✔" : "&cNIE &l✘"));
                                        s = s.replace("{STATUS_DROP}", (!user.hasDisable(stoneDataArray.getId()) ? "&aTAK &l✔" : "&cNIE &l✘"));
                                        s = s.replace("{STATUS_MESSAGE}", (user.hasDisableMessage(stoneDataArray.getId()) ? "&aTAK &l✔" : "&cNIE &l✘"));
                                        return s;
                                    }).collect(Collectors.toList()));


                    if (!user.hasDisable(stoneDataArray.getId())) {
                        dropItem.visibleFlag().addEnchant(Enchantment.DIG_SPEED, 10);
                    }

                    inventory.setItem(stoneDataArray.getInventorySlot(), dropItem.toItemStack());

                } catch (Exception e) {
                    player.closeInventory();
                    player.sendTitle("", MessageHelper.translateText("&4Błąd: &cW konfiguracji drop id&8: &d" + stoneDataArray.getId()));
                    return;
                }
            }
            inventory.setItem(38, new ItemHelper(Material.GOLD_PICKAXE).visibleFlag().addEnchant(Enchantment.ARROW_FIRE, 10)
                    .setName("&5TurboDrop")
                    .addLore("")
                    .addLore("&8>> &fDla ciebie&8: &cNIE &l✘")
                    .addLore("&8>> &fDla serwera&8: &cNIE &l✘").toItemStack());

            inventory.setItem(42, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setName("&aWłącz wszystkie dropy")
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=").toItemStack());

            inventory.setItem(43, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                    .setName("&cWyłącz wszystkie dropy")
                    .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0=").toItemStack());

        });
        this.onClick(player, event -> {
            event.setCancelled(true);


            if(event.getSlot() == 49){
                player.closeInventory();
                new DropInventory().show(player);
            }

            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                if(event.getSlot() == 43){
                    user.disableALLDrop(this.stoneDropData.getStoneDataArrays());
                    this.show(player);
                    return;
                }
                if(event.getSlot() == 42){
                    user.enableALLDrop();
                    this.show(player);
                    return;
                }
                ItemStack currentItem = event.getCurrentItem();
                if(!ItemIdentityHelper.compareIdentity(currentItem, "id"))return;
                int key = Integer.parseInt(ItemIdentityHelper.getItemIdentityTagName(currentItem, "id"));
                this.stoneDropData.findDropStoneDataById(key).ifPresent(stoneDropData -> {

                    switch (event.getClick()){
                        case LEFT:{
                            if(!user.hasDisable(stoneDropData.getId())){
                                user.disableDrop(stoneDropData.getId());
                                this.show(player);
                                return;
                            }
                            user.enableDrop(stoneDropData.getId());
                            this.show(player);
                            break;
                        }
                        case RIGHT:{
                            if(!user.hasDisableMessage(stoneDropData.getId())){
                                user.disableDropMessage(stoneDropData.getId());
                                this.show(player);
                                return;
                            }
                            user.enableDropMessage(stoneDropData.getId());
                            this.show(player);
                            break;
                        }
                    }
                });

            });
        });

    }
}
