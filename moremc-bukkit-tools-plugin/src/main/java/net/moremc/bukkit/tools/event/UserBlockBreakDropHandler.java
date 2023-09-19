package net.moremc.bukkit.tools.event;

import net.moremc.api.API;
import net.moremc.api.data.drop.stone.DropStoneData;
import net.moremc.api.data.drop.stone.DropStoneDataArray;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.bukkit.api.utilities.RandomUtilities;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import net.moremc.sectors.event.UserBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class UserBlockBreakDropHandler implements Consumer<UserBlockBreakEvent> {

    private final DropStoneData dropStoneData = CommunicatorPlugin.getInstance().getDropStoneData();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final SectorService sectorService = API.getInstance().getSectorService();

    @Override
    public void accept(UserBlockBreakEvent userBlockBreakEvent) {
        BlockBreakEvent event = userBlockBreakEvent.getBlockBreakEvent();
        Player player = event.getPlayer();


        if (event.getBlock().getType().equals(Material.OBSIDIAN)) {
            userBlockBreakEvent.getOptionalUser().ifPresent(user -> {
                user.setBreakObsidian(user.getBreakObsidian() + 1);
            });
        }

        userBlockBreakEvent.getOptionalUser().ifPresent(user -> {
            if (this.sectorService.getCurrentSector().get().isSpawn() && !UserGroupType.hasPermission(UserGroupType.ROOT, user)) {
                event.setCancelled(true);
                player.sendMessage(MessageHelper.translateText("&cZnajdujesz się na spawnie."));
                return;
            }
            if (event.getBlock().getType().equals(Material.STONE)) {
                Block block = event.getBlock();
                Block blockUpperOne = block.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                Block blockUpperTwo = block.getLocation().subtract(0.0, 2.0, 0.0).getBlock();

                Bukkit.getScheduler().runTaskLater(ToolsPlugin.getInstance(), () -> {
                    if (!blockUpperOne.getType().equals(Material.ENDER_STONE) && !blockUpperTwo.getType().equals(Material.ENDER_STONE))
                        return;
                    block.setType(Material.STONE);
                }, 25L);

                this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {

                    event.getBlock().setType(Material.AIR);
                    player.giveExp(15);
                    user.setBreakStone(user.getBreakStone() + 1);
                    for (DropStoneDataArray dataDrop : this.dropStoneData.getStoneDataArrays()) {
                        if (!RandomUtilities.getChance(dataDrop.getChance())) continue;
                        if (player.getGameMode().equals(GameMode.CREATIVE)) continue;
                        if (user.hasDisable(dataDrop.getId())) continue;

                        ItemMeta itemMeta = (!player.getItemInHand().equals(Material.AIR) ? player.getItemInHand().getItemMeta() : null);

                        int amount = 1;
                        if (itemMeta != null && itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 1) {
                            amount = RandomUtilities.getRandInt(1, 3);
                            if (dataDrop.getMaterialName().equalsIgnoreCase("COBBLESTONE")) amount = 1;
                        }
                        ItemUtilities.addItem(player, new ItemHelper(Material.valueOf(dataDrop.getMaterialName()), amount));
                        if (!user.hasDisableMessage(dataDrop.getId())) continue;
                        bukkitUser.updateActionBar(UserActionBarType.DROP, "&fTrafiłeś&8(" + dataDrop.getMaterialPolishName() + "&7, &d" + amount + "szt&7, &d" + dataDrop.getChance() + "%&8)", 2);
                    }
                });
            }
        });
    }
}
