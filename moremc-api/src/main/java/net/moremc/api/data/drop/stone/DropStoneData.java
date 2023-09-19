package net.moremc.api.data.drop.stone;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

public class DropStoneData implements Serializable {

    private String inventoryName;
    private String[] inventoryLore;
    private DropStoneDataArray[] stoneDataArrays;

    public String getInventoryName() {
        return inventoryName;
    }

    public String[] getInventoryLore() {
        return inventoryLore;
    }

    public DropStoneDataArray[] getStoneDataArrays() {
        return stoneDataArrays;
    }
    public Optional<DropStoneDataArray> findDropStoneDataById(int id){
        return Arrays.stream(this.stoneDataArrays)
                .filter(stoneDataArray -> stoneDataArray.getId() == id)
                .findFirst();
    }

    @Override
    public String toString() {
        return "DropStoneData{" +
                "inventoryName='" + inventoryName + '\'' +
                ", inventoryLore=" + Arrays.toString(inventoryLore) +
                ", stoneDataArrays=" + Arrays.toString(stoneDataArrays) +
                '}';
    }
}
