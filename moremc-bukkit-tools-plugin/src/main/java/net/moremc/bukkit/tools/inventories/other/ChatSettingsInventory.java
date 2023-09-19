package net.moremc.bukkit.tools.inventories.other;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.user.UserSettingMessage;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class ChatSettingsInventory extends InventoryHelperImpl {

    private final UserService userService = API.getInstance().getUserService();

    public ChatSettingsInventory() {
        super("&dZarządzaj ustawieniami czatu", 27);
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        Integer[] glassYellowSlots = new Integer[]{0, 2, 4, 7, 17, 18, 19, 21, 23, 25};
        Integer[] glassDarkYellowSlots = new Integer[]{1, 3, 5, 6, 8, 9, 20, 22, 24, 26};

        Arrays.stream(glassYellowSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(glassDarkYellowSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 10).setName(" ").toItemStack()));

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            for (UserSettingMessage userSettingMessage : user.getUserSettingList()) {
                ItemHelper itemSetting = new ItemHelper(Material.BOOK)
                        .setName("&8&m---&5&m---&d&m---&8&m---&a&m---&8&m--&f&m---")
                        .addLore("&f" + userSettingMessage.getMessagePolishName())
                        .addLore("    " + (userSettingMessage.isStatus() ? "&a&lAKTYWNY" : "&c&lWYLĄCZONE"))
                        .addLore("&8&m---&5&m---&d&m---&8&m---&a&m---&8&m--&f&m---");
                inventory.setItem(userSettingMessage.getSlot(), itemSetting.toItemStack());
            }
            for (UserSettingMessage userSettingMessage : user.getUserSettingList()) {
                ItemHelper itemSetting = new ItemHelper(Material.getMaterial(351), 1, (short) (userSettingMessage.isStatus() ? 5 : 8))
                        .setName((userSettingMessage.isStatus() ? "&a&lAKTYWNY" : "&c&lWYLĄCZONE"));
                inventory.setItem(userSettingMessage.getSlot() + 9, itemSetting.toItemStack());
            }
        });
        this.onClick(player, event -> {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                event.setCancelled(true);

                user.findUserSettingBySlot(event.getSlot()).ifPresent(userSettingMessage -> {
                    userSettingMessage.setStatus(user, !userSettingMessage.isStatus());
                    this.show(player);
                });
            });
        });
    }
}
