package net.moremc.bukkit.tools.listeners.player;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.BackPackService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.generator.player.GeneratorPlayer;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.backpack.BackPackInventory;
import net.moremc.bukkit.tools.inventories.backpack.BackPackSlittingInventory;
import net.moremc.bukkit.tools.runnable.other.ChestAnimationRunnable;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class PlayerInteractEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();
    private final BackPackService backPackService = API.getInstance().getBackPackService();
    private final GeneratorPlayer generatorPlayer = new GeneratorPlayer();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final SectorService sectorService = API.getInstance().getSectorService();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = userService.findByValue(player.getName());


        Block block = event.getClickedBlock();


        if (this.sectorService.getCurrentSector().get().isSpawn()) {
            if (block != null && block.getType().equals(Material.ITEM_FRAME)) {
                event.setCancelled(true);
                player.sendMessage(MessageHelper.translateText("&cZnajdujesz się na spawnie."));
            }
        }

        ItemStack item = event.getItem();
        if (item == null) return;


        NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
        if (tag != null) {

            if (item.getType().equals(Material.SHEARS)) {
                if (tag.hasKey("backpack-shears")) {
                    BackPackSlittingInventory slittingInventory = new BackPackSlittingInventory();
                    slittingInventory.show(player);
                }
                return;
            }
            if (!item.getType().equals(Material.SKULL_ITEM)) return;
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (tag.hasKey("backpack-uuid")) {
                    event.setCancelled(true);
                    UUID backPackUUID = UUID.fromString(tag.getString("backpack-uuid"));
                    backPackService.findByValueOptional(backPackUUID).ifPresent(backPack -> {
                        if (!backPack.getOwnerUUID().equals(user.getUuid())) {
                            player.sendMessage(MessageHelper.colored("&cNie jesteś właścicielem tego plecaka"));
                            return;
                        }
                        if (!user.hasCooldown("backpack")) {
                            player.sendMessage(MessageHelper.colored("&cPlecak możesz otworzyć za &7" + DataHelper.getTimeToString(user.getCooldownTime("backpack")) + "&c!"));
                            return;
                        }
                        user.addCooldown("backpack", System.currentTimeMillis() + 5000);
                        BackPackInventory backPackInventory = new BackPackInventory(backPack);
                        backPackInventory.show(player);
                    });
                }
                if (tag.hasKey("case") && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {
//                        if (bukkitUser.isOpenCase()) {
//                            event.setCancelled(true);
//                            player.sendMessage(MessageHelper.translateText("&cOtwierasz już skrzynke..."));
//                            return;
//                        }
                        new ChestAnimationRunnable(bukkitUser, event.getClickedBlock()).runTaskTimer(ToolsPlugin.getInstance(), 0L, 2L);
                        ItemUtilities.remove(new ItemHelper(Material.SKULL_ITEM, 1, (short) 3).setName("&d&lMAGICZNA SKRZYNIA").toItemStack(), player, 1);
                        bukkitUser.setOpenCase(true);
                        event.setCancelled(true);
                    });
                }
            }
        }
    }
}