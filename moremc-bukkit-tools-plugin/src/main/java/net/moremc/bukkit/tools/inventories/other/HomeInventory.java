package net.moremc.bukkit.tools.inventories.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.user.UserHome;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.TeleportHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;

import java.util.Arrays;

public class HomeInventory extends InventoryHelperImpl {

    private final UserService userService = API.getInstance().getUserService();

    public HomeInventory() {
        super("&dLista dostępnych domów", 45);
    }

    @Override
    protected void initializeInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 7).setName(" ").toItemStack());
        }
        Integer[] yellowGlassSlots = new Integer[]{0, 1, 3, 5, 7, 8, 9, 17, 18, 27, 28, 35, 36, 37, 39, 41, 43, 44};
        Integer[] grayGlassSlots = new Integer[]{2, 4, 6, 10, 11, 12, 13, 14, 15, 16, 18, 19, 25, 26, 28, 29, 30, 31, 32, 33, 34, 38, 42};

        Arrays.stream(yellowGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(grayGlassSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack()));
        inventory.setItem(4, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());
        inventory.setItem(40, new ItemHelper(Material.BARRIER).setName(" ").toItemStack());


        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            for (UserHome userHome : user.getUserHomeList()) {
                ItemHelper homeItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                        .setName("&fID: &5#&d" + userHome.getId())
                        .setOwnerUrl((userHome.hasSet() ?
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI1ODcxYzcyOTg3MjY2ZTE1ZjFiZTQ5YjFlYzMzNGVmNmI2MThlOTY1M2ZiNzhlOTE4YWJkMzk1NjNkYmI5MyJ9fX0=" :
                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2VmMTE5ZjA4ODUxYTcyYTVmMTBmYmMzMjQ3ZDk1ZTFjMDA2MzYwZDJiNGY0MTJiMjNjZTA1NDA5Mjc1NmIwYyJ9fX0="))
                        .setLore(Arrays.asList(
                                "",
                                " &fDostęp: " + (!UserGroupType.hasPermission(userHome.getPermission(), user) ? "&cNIE" : "&aTAK") + " &8(" + userHome.getPermission().getPrefix() + "&8)",
                                " &fUstawiono: " + (!userHome.hasSet() ? "&cNIE" : "&aTAK"),
                                "",
                                "&5Prawy &8- &fAby ustawić ten dom.",
                                "&5Lewy &8- &fAby przeteleportować się."
                        ));

                inventory.setItem(userHome.getInventorySlot(), homeItem.toItemStack());
            }
        });
        this.onClick(player, event -> {
            event.setCancelled(true);

            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                user.findHomeByInventorySlot(event.getSlot()).ifPresent(userHome -> {

                    switch (event.getClick()){
                        case RIGHT:{
                            if(!UserGroupType.hasPermission(userHome.getPermission(), user)){
                                player.closeInventory();
                                player.sendMessage(MessageHelper.translateText("&cNie posiadasz dostępu do tej akcji."));
                                return;
                            }
                            Location location =  player.getLocation();
                            userHome.setLocation(user, new LocationSerializer(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                                    location.getYaw(), location.getPitch()));
                            player.closeInventory();
                            player.sendMessage(MessageHelper.translateText("&fPomyślnie &dustawiono &fdom pod ID: &5#&d" + userHome.getId()));
                            break;
                        }
                        case LEFT:{
                            if(!userHome.hasSet()){
                                player.closeInventory();
                                player.sendMessage(MessageHelper.translateText("&cMusisz najpierw ustawić ten dom"));
                                return;
                            }
                            Location location =  new Location(Bukkit.getWorld(userHome.getLocation().getWorld()), userHome.getLocation().getX(), userHome.getLocation().getY(), userHome.getLocation().getZ(), userHome.getLocation().getYaw(), userHome.getLocation().getPitch());
                            TeleportHelper.teleport(player, System.currentTimeMillis() + TimeType.SECOND.getTime(5), location);
                            player.closeInventory();
                        }
                    }
                });

            });

        });

    }
}
