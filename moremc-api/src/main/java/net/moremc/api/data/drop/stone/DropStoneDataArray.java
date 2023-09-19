package net.moremc.api.data.drop.stone;

import java.io.Serializable;

public class DropStoneDataArray implements Serializable {


    private int id;
    private String materialName;
    private String materialPolishName;
    private int inventorySlot;
    private double chance;
    private boolean fortune;

    public int getId() {
        return id;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getMaterialName() {
        return materialName;
    }

    public double getChance() {
        return chance;
    }

    public boolean isFortune() {
        return fortune;
    }

    public String getMaterialPolishName() {
        return materialPolishName;
    }

    @Override
    public String toString() {
        return "DropStoneDataArray{" +
                "id=" + id +
                ", materialName='" + materialName + '\'' +
                ", materialPolishName='" + materialPolishName + '\'' +
                ", inventorySlot=" + inventorySlot +
                ", chance=" + chance +
                ", fortune=" + fortune +
                '}';
    }
}
