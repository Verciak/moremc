package net.moremc.bukkit.tools.inventories.kit;

import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;

public class KitInventory extends InventoryHelperImpl {


    private final Kit kit;
    private final UserService userService = API.getInstance().getUserService();

    public KitInventory(Kit kit) {
        super("&dZestaw: &f" + kit.getName(), 54);
        this.kit = kit;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        inventory.setContents(ItemSerializer.encodeItem(this.kit.getInventoryGlassSerialized()));

        for (ItemStack itemStack : ItemSerializer.encodeItem(this.kit.getInventoryItemsSerialized())) {
            if(itemStack == null || itemStack.getType().equals(Material.AIR))continue;
            inventory.addItem(itemStack);
        }
        inventory.setItem(4, new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setName("&aOdbierz zestaw")
                .setOwnerUrl("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=").toItemStack());

        this.onClick(player, event -> {
            event.setCancelled(true);

            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if(event.getSlot() == 4) {

                    if(!UserGroupType.hasPermission(this.kit.getPermissionType(), user)){
                        player.sendTitle("", MessageHelper.translateText("&cZestaw wymaga rangi&8: " + this.kit.getPermissionType().getPrefix()));
                        return;
                    }

                    if (!user.hasCooldown("kit_" + this.kit.getName())) {
                        player.closeInventory();
                        player.sendTitle("", MessageHelper.translateText("&cNastępny zestaw dostępny za&8: &4" + DataHelper.getTimeToString(user.getCooldownTime("kit_" + kit.getName()))));
                        return;
                    }
                    for (ItemStack itemStack : ItemSerializer.encodeItem(this.kit.getInventoryItemsSerialized())) {
                        if(itemStack == null || itemStack.getType().equals(Material.AIR))continue;
                        ItemUtilities.addItem(player, itemStack);
                    }
                    player.closeInventory();
                    player.sendTitle("", MessageHelper.translateText("&aPomyślnie odebrano zestaw&8(&a" + this.kit.getName() + "&8)"));
                    user.addCooldown("kit_" + this.kit.getName(), System.currentTimeMillis() + TimeType.MINUTE.getTime(this.kit.getDelayTime()));
                }
            });
        });

    }
}
