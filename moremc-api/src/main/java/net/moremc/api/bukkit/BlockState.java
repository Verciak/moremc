package net.moremc.api.bukkit;

import net.moremc.api.serializer.LocationSerializer;

import java.io.Serializable;

public class BlockState implements Serializable {


    private final String materialName;
    private final byte data;
    private final LocationSerializer location;

    public BlockState(String materialName, byte data, LocationSerializer location){
        this.materialName = materialName;
        this.data = data;
        this.location = location;
    }
    public String getMaterialName() {
        return materialName;
    }
    public byte getData() {
        return data;
    }
    public LocationSerializer getLocation() {
        return location;
    }
}
