package net.moremc.bukkit.tools.runnable.other;

import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import net.moremc.api.data.drop.chest.DropChestData;
import net.moremc.api.data.drop.chest.DropChestDataArray;
import net.moremc.api.helper.type.TimeType;
import net.moremc.bukkit.api.cache.entity.BukkitUser;
import net.moremc.bukkit.api.helper.ItemParser;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.type.SendType;
import net.moremc.bukkit.api.utilities.RandomUtilities;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.util.ArrayList;
import java.util.Collections;

public class ChestAnimationRunnable extends BukkitRunnable {

    private final DropChestData chestData = CommunicatorPlugin.getInstance().getDropChestData();
    private static float yaw = 0;
    private static double height = (float) -0.25;
    private static boolean add = true;

    private final BukkitUser bukkitUser;
    private final Block block;
    private final long timeDelay = System.currentTimeMillis() + TimeType.SECOND.getTime(3);

    public ChestAnimationRunnable(BukkitUser bukkitUser, Block block) {
        this.bukkitUser = bukkitUser;
        this.block = block;
    }

    @Override
    public void run() {
        yaw = (float) (yaw + 3.16);
        if (yaw >= 360) yaw = 0;
        height = height + (add ? 0.025 : -0.025);
        if (height >= 0.25 || height <= -0.25) add = !add;


        int randomNumber = RandomUtilities.getRandInt(0, this.chestData.getChestDataArrays().length - 1);
        DropChestDataArray drop = this.chestData.getChestDataArrays()[randomNumber];

        bukkitUser.getArmorStandHelperList().get(3).apply(armorStandHelper -> {
            if (timeDelay <= System.currentTimeMillis()) {
                this.cancel();
                armorStandHelper.send(SendType.REMOVE, block.getLocation(), "");
                armorStandHelper.setEntity(new ArrayList<>());

                bukkitUser.setOpenCase(false);
                bukkitUser.getPlayer().sendTitle(
                        MessageHelper.colored(" &d🔮 &fWYGRANA &d🔮"),
                        MessageHelper.colored(" " + drop.getInventoryName() +" "
                ));

                ItemUtilities.addItem(bukkitUser.getPlayer(), ItemParser.parseItem(drop.getItemParser()));
                bukkitUser.getPlayer().playSound(block.getLocation(), Sound.ITEM_PICKUP, 10f, 10f);
                return;
            }
            armorStandHelper.setLocation(block.getLocation());
            armorStandHelper.setDisplayName(Collections.singletonList(drop.getInventoryName()));

            Location location = armorStandHelper.getLocation();
            location.setYaw(yaw);
            location.setPitch(yaw - bukkitUser.getPlayer().getLocation().getPitch());
            location.setY(block.getY() + height);

            armorStandHelper.send(SendType.UPDATE, location, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQwNmU0NTMxOGU5YTRhNmJmZTEzMmYyMDJmZTNjZWFjMTVkMTFlYWVkYmVmMWViMDZhMzc2ZGI0MzMwOTBhOCJ9fX0=");
        });
    }
}
