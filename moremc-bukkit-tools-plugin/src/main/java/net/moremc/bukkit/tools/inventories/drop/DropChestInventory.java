package net.moremc.bukkit.tools.inventories.drop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.data.drop.chest.DropChestData;
import net.moremc.api.data.drop.chest.DropChestDataArray;
import net.moremc.api.data.drop.chest.type.DropChestDataItemType;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemParser;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DropChestInventory extends InventoryHelperImpl {


    private final DropChestData chestData = CommunicatorPlugin.getInstance().getDropChestData();

    public DropChestInventory() {
        super("&dDrop z case", 54);
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


        for (DropChestDataArray chestDataArray : this.chestData.getChestDataArrays()) {
            ItemHelper chestItem = ItemParser.parseItem(chestDataArray.getItemParser())
                    .setName(chestDataArray.getInventoryName())
                    .setLore(Arrays.stream(this.chestData.getInventoryLore())
                            .map(s -> {
                                 s= s.replace("{CHANCE}", new DecimalFormat("##.##").format(chestDataArray.getChance()));
                                return s;
                            }).collect(Collectors.toList()));

            if(chestDataArray.getItemType().equals(DropChestDataItemType.VOUCHER)){
                chestItem.visibleFlag().addEnchant(Enchantment.ARROW_FIRE, 10);
            }

            inventory.setItem(chestDataArray.getInventorySlot(), chestItem.toItemStack());
        }


        this.onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 49){
                player.closeInventory();
                new DropInventory().show(player);
            }
        });
    }
}
