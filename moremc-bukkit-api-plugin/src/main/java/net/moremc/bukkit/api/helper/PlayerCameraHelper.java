package net.moremc.bukkit.api.helper;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerCameraHelper {

    public static void setCamera(Player player, Entity entity) {
        try {
            PacketPlayOutCamera packetPlayOutCamera = new PacketPlayOutCamera(entity);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutCamera);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void removeCamera(Player player) {
        setCamera(player, ((CraftPlayer) player).getHandle());
    }
}
