package net.moremc.bukkit.tools.inventories.drop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.data.drop.cobblex.DropCobblexData;
import net.moremc.api.data.drop.cobblex.DropCobblexDataArray;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemParser;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DropCobblexInventory extends InventoryHelperImpl {

    private final DropCobblexData cobblexData = CommunicatorPlugin.getInstance().getDropCobblexData();

    public DropCobblexInventory() {
        super("&dDrop z cobblex", 54);
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

        for (DropCobblexDataArray cobblexDataArray : this.cobblexData.getDropCobblexDataArrays()) {
            ItemHelper cobblexItem = ItemParser.parseItem(cobblexDataArray.getItemParser())
                    .setName(cobblexDataArray.getInventoryName())
                    .setLore(Arrays.stream(this.cobblexData.getInventoryLore())
                            .map(s -> {
                                s= s.replace("{CHANCE}", new DecimalFormat("##.##").format(cobblexDataArray.getChance()));
                                return s;
                            }).collect(Collectors.toList()));

            inventory.setItem(cobblexDataArray.getInventorySlot(), cobblexItem.toItemStack());
        }
        onClick(player, event -> {
            event.setCancelled(true);

            if(event.getSlot() == 49){
                player.closeInventory();
                new DropInventory().show(player);
            }
        });
    }
}
