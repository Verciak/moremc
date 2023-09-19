package net.moremc.api.sector;

import net.moremc.api.sector.info.SectorInfo;
import net.moremc.api.sector.info.SectorLocationInfo;
import net.moremc.api.sector.type.SectorType;

import java.io.Serializable;

public class Sector implements Serializable {

    private final String name;
    private final SectorInfo info;

    public Sector(String name, SectorType type, SectorLocationInfo locationInfo) {
        this.name = name;
        this.info = new SectorInfo(type, locationInfo);
    }
    public SectorInfo getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public boolean isSpawn() {
        return this.info.getType().equals(SectorType.SPAWN);
    }

    public boolean isTeleport() {
        return this.info.getType().equals(SectorType.TELEPORT);
    }

    public boolean isCustom() {
        return this.info.getType().equals(SectorType.CUSTOM);
    }
}
