package net.moremc.sectors.holder;

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.moremc.api.API;

import java.util.ArrayList;
import java.util.List;

public class BorderHolder {

    public static void update(Player player) {
        API.getInstance().getSectorService().getCurrentSector().ifPresent(sector -> {

            final List<Location> locations = new ArrayList<>();
            final Location sectorCenter = new Location(player.getWorld(), (sector.getInfo().getLocationInfo().getMinX() + sector.getInfo().getLocationInfo().getMaxX()) / 2, 80, (sector.getInfo().getLocationInfo().getMinZ() + sector.getInfo().getLocationInfo().getMaxZ()) / 2);
            locations.add(new Location(Bukkit.getWorld(sector.getInfo().getLocationInfo().getWorldName()), sector.getInfo().getLocationInfo().getMinX(), 64, sector.getInfo().getLocationInfo().getMinZ()));
            locations.add(new Location(Bukkit.getWorld(sector.getInfo().getLocationInfo().getWorldName()), sector.getInfo().getLocationInfo().getMaxX(), 64, sector.getInfo().getLocationInfo().getMinZ()));
            locations.add(new Location(Bukkit.getWorld(sector.getInfo().getLocationInfo().getWorldName()), sector.getInfo().getLocationInfo().getMinX(), 64, sector.getInfo().getLocationInfo().getMaxZ()));
            locations.add(new Location(Bukkit.getWorld(sector.getInfo().getLocationInfo().getWorldName()), sector.getInfo().getLocationInfo().getMaxX(), 64, sector.getInfo().getLocationInfo().getMaxZ()));
            double distance = Double.MAX_VALUE;
            final int increase = 99999999;
            Location side = null;
            for (final Location loc : locations) {
                if (!loc.getWorld().equals(player.getWorld())) {
                    continue;
                }
                double newDistance = loc.distance(player.getLocation());
                if (newDistance < distance) {
                    distance = newDistance;
                    side = loc;
                }
            }
            if (side == null) {
                return;
            }
            double offset = 0.5;
            if (side.getX() >= 3000) {
                side.setX(side.getX() - offset);
            } else if (side.getX() <= 3000 * -1) {
                side.setX(side.getX() + offset);
            }
            if (side.getZ() >= 3000) {
                side.setZ(side.getZ() - offset);
            } else if (side.getZ() <= 3000 * -1) {
                side.setZ(side.getZ() + offset);
            }
            final Location borderLoc = side;
            if (side.getZ() < sectorCenter.getZ()) {
                side.setZ(side.getZ() - offset);
                borderLoc.setZ(side.getZ() + increase - offset);
            } else {
                side.setZ(side.getZ() + offset);
                borderLoc.setZ(side.getZ() - increase + offset);
            }
            if (side.getX() < sectorCenter.getX()) {
                side.setX(side.getX() - offset);
                borderLoc.setX(side.getX() + increase - offset);
            } else {
                side.setX(side.getX() + offset);
                borderLoc.setX(side.getX() - increase + offset);
            }
            WorldBorder worldBorder = new WorldBorder();
            worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();
            worldBorder.setSize(increase * 2);
            worldBorder.transitionSizeBetween(worldBorder.getSize(), worldBorder.getSize() - 0.5, Long.MAX_VALUE);
            worldBorder.setCenter(borderLoc.getX(), borderLoc.getZ());
            PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });
    }
}