package net.moremc.api.data.drop.chest;


import net.moremc.api.data.drop.chest.type.DropChestDataItemType;

import java.io.Serializable;

public class DropChestDataArray implements Serializable{


    private int id;
    private String inventoryName;
    private int inventorySlot;
    private double chance;
    private String itemParser;
    private DropChestDataItemType itemType;

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

    public DropChestDataItemType getItemType() {
        return itemType;
    }

    public double getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return "DropChestDataArray{" +
                "id=" + id +
                ", inventoryName='" + inventoryName + '\'' +
                ", inventorySlot=" + inventorySlot +
                ", chance=" + chance +
                ", itemParser='" + itemParser + '\'' +
                ", itemType=" + itemType +
                '}';
    }
}
