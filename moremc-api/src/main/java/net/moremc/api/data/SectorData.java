package net.moremc.api.data;

import net.moremc.api.sector.info.SectorLocationInfo;
import net.moremc.api.sector.type.SectorType;

import java.io.Serializable;

public class SectorData implements Serializable {

    private String name;
    private SectorType type;
    private SectorLocationInfo locationInfo;

    public SectorLocationInfo getLocationInfo() {
        return locationInfo;
    }

    public SectorType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
