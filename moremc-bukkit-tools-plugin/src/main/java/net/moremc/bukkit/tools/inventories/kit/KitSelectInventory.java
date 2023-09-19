package net.moremc.bukkit.tools.inventories.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.service.entity.KitService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class KitSelectInventory extends InventoryHelperImpl {


    private final KitService kitService = API.getInstance().getKitService();

    public KitSelectInventory() {
        super("&dZestawy serwera", 54);
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

        for (Kit kit : this.kitService.getMap().values()) {
            ItemHelper kitItem = new ItemHelper(Material.valueOf(kit.getMaterialInventory()))
                    .setIdentifyItem("name", kit.getName())
                    .setName("&aZestaw: &7" + kit.getName())
                    .addLore("")
                    .addLore(" &fMożliwość odbirou co: &d" + DataHelper.getTimeToString(System.currentTimeMillis() + TimeType.MINUTE.getTime(kit.getDelayTime())))
                    .addLore("")
                    .addLore("&5Lewy &8- &fAby przejść dalej.");

            inventory.setItem(kit.getInventorySlot(), kitItem.toItemStack());
        }
        this.onClick(player, event -> {

            event.setCancelled(true);

            ItemStack currentItem = event.getCurrentItem();
            if(!ItemIdentityHelper.compareIdentity(currentItem, "name"))return;
            String key  =ItemIdentityHelper.getItemIdentityTagName(currentItem, "name");

            this.kitService.findByValueOptional(key).ifPresent(kit -> {
                KitInventory kitInventory = new KitInventory(kit);
                kitInventory.show(player);
            });

        });


    }
}
