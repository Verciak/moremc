package net.moremc.api.data.drop.cobblex;

import java.io.Serializable;

public class DropCobblexDataArray implements Serializable {


    private int id;
    private String inventoryName;
    private int inventorySlot;
    private double chance;
    private String itemParser;

    public String getInventoryName() {
        return inventoryName;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public int getId() {
        return id;
    }

    public String getItemParser() {
        return itemParser;
    }

    public double getChance() {
        return chance;
    }


    @Override
    public String toString() {
        return "DropCobblexDataArray{" +
                "id=" + id +
                ", inventoryName='" + inventoryName + '\'' +
                ", inventorySlot=" + inventorySlot +
                ", chance=" + chance +
                ", itemParser='" + itemParser + '\'' +
                '}';
    }
}
