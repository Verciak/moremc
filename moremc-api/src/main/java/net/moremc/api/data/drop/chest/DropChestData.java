package net.moremc.api.data.drop.chest;

import java.io.Serializable;
import java.util.Arrays;

public class DropChestData implements Serializable {

    private String[] inventoryLore;
    private DropChestDataArray[] chestDataArrays;


    public String[] getInventoryLore() {
        return inventoryLore;
    }

    public DropChestDataArray[] getChestDataArrays() {
        return chestDataArrays;
    }

    @Override
    public String toString() {
        return "DropChestData{" +
                "inventoryLore=" + Arrays.toString(inventoryLore) +
                ", chestDataArrays=" + Arrays.toString(chestDataArrays) +
                '}';
    }
}
