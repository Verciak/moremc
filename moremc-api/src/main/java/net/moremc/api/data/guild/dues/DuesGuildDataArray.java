package net.moremc.api.data.guild.dues;


import java.io.Serializable;
import java.util.Arrays;

public class DuesGuildDataArray implements Serializable {


    public String name;
    public String inventoryName;
    public String[] inventoryLore;
    public int inventorySlot;
    public String materialName;


    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public String[] getInventoryLore() {
        return inventoryLore;
    }

    public String getName() {
        return name;
    }


    public String getMaterialName() {
        return materialName;
    }

    @Override
    public String toString() {
        return "DuesGuildData{" +
                "name='" + name + '\'' +
                ", inventoryName='" + inventoryName + '\'' +
                ", inventoryLore=" + Arrays.toString(inventoryLore) +
                ", inventorySlot=" + inventorySlot +
                ", materialName='" + materialName + '\'' +
                '}';
    }
}
