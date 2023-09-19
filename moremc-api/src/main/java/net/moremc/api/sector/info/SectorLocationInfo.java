package net.moremc.api.sector.info;

import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectorLocationInfo implements Serializable {


    private final String worldName;
    private final int minX;
    private final int maxX;
    private final int minZ;
    private final int maxZ;

    public SectorLocationInfo(String worldName, int minX, int maxX, int minZ, int maxZ) {
        this.worldName = worldName;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }
    public String getWorldName() {
        return worldName;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public boolean isInSector(int x, int z, String world) {
        if (!world.equals(this.worldName)) {
            return false;
        }
        return x >= this.minX &&
                z >= this.minZ &&
                x < this.maxX &&
                z < this.maxZ;
    }
    public int getDistanceToBorder(int x, int z) {
        double x1 = Math.abs(x - this.minX);
        double x2 = Math.abs(x - this.maxX);
        double z1 = Math.abs(z - this.minZ);
        double z2 = Math.abs(z - this.maxZ);
        return (int) Math.min(Math.min(x1, x2), Math.min(z1, z2));
    }

    public Sector getNearestSector(double distance, int x, int z, String world, SectorService sectorService) {
        double border = distance + 5;

        List<Optional<Sector>> sectors = new ArrayList<>();

        sectors.add(sectorService.findSectorByLocation(world, (int) (x + border), z));
        sectors.add(sectorService.findSectorByLocation(world, (int) (x - border), z));
        sectors.add(sectorService.findSectorByLocation(world, x, (int) (z + border)));
        sectors.add(sectorService.findSectorByLocation(world, x, (int) (z - border)));

        for (Optional<Sector> sectorOptional : sectors) {
            if(!sectorOptional.isPresent())continue;

            Sector sector = sectorOptional.get();
            if (!this.equals(sector.getInfo().getLocationInfo()) && sector.getInfo().getLocationInfo().getWorldName().equals(this.worldName)) {
                return sector;
            }
        }

        return null;
    }
}
