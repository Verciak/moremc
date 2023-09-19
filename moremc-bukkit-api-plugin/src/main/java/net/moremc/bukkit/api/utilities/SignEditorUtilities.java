package net.moremc.bukkit.api.utilities;

import net.minecraft.server.v1_8_R3.*;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SignEditorUtilities {

    public static void openSignEditor(Player player, Sign s) {
        Location loc = s.getLocation();
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        TileEntitySign tileEntitySign = (TileEntitySign) nmsPlayer.world.getTileEntity(pos);
        PlayerConnection conn = nmsPlayer.playerConnection;
        tileEntitySign.isEditable = true;
        tileEntitySign.a(nmsPlayer);
        conn.sendPacket(new PacketPlayOutOpenSignEditor(pos));
    }

    public static void openSignEditorToPlayer(Player player, String line0, String line1, String line2, String line3) {
        Location newSign = player.getLocation().add(0.0, 10.0, 0.0);
        Location fixnewSign = player.getLocation().add(0.0, 9.0, 0.0);
        fixnewSign.getBlock().setType(org.bukkit.Material.BEDROCK);
        newSign.getBlock().setType(org.bukkit.Material.SIGN_POST);
        org.bukkit.block.Block sign = newSign.getBlock();
        org.bukkit.block.BlockState signState = sign.getState();
        Sign signBlock = (Sign) signState;
        signBlock.setLine(0, MessageHelper.translateText(line0));
        signBlock.setLine(1, MessageHelper.translateText(line1));
        signBlock.setLine(2, MessageHelper.translateText(line2));
        signBlock.setLine(3,MessageHelper.translateText(line3));
        signBlock.update();


        new BukkitRunnable() {
            @Override
            public void run() {
                openSignEditor(player, signBlock);
            }
        }.runTaskLaterAsynchronously(BukkitAPI.getInstance(), 3L);
    }
}