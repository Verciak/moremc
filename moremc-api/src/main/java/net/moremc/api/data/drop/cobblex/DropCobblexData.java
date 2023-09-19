package net.moremc.api.data.drop.cobblex;

import java.io.Serializable;
import java.util.Arrays;

public class DropCobblexData implements Serializable {

    private String[] inventoryLore;
    private DropCobblexDataArray[] dropCobblexDataArrays;

    public String[] getInventoryLore() {
        return inventoryLore;
    }

    public DropCobblexDataArray[] getDropCobblexDataArrays() {
        return dropCobblexDataArrays;
    }

    @Override
    public String toString() {
        return "DropCobblexData{" +
                "inventoryLore=" + Arrays.toString(inventoryLore) +
                ", dropCobblexDataArrays=" + Arrays.toString(dropCobblexDataArrays) +
                '}';
    }
}
