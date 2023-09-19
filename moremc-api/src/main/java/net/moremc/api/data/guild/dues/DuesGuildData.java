package net.moremc.api.data.guild.dues;


import java.io.Serializable;
import java.util.Arrays;

public class DuesGuildData implements Serializable {


    public DuesGuildDataArray[] duesGuildDatumArrays;


    public DuesGuildDataArray[] getDuesGuildData() {
        return duesGuildDatumArrays;
    }

    @Override
    public String toString() {
        return "DuesGuildJson{" +
                "duesGuildData=" + Arrays.toString(duesGuildDatumArrays) +
                '}';
    }
    public DuesGuildDataArray findDuesGuildByName(String duesName){
        return Arrays.stream(this.duesGuildDatumArrays).filter(duesGuild -> duesGuild.getName().equalsIgnoreCase(duesName)).findFirst().orElse(null);
    }
    public DuesGuildDataArray findDuesGuildByInventorySlot(int inventorySlot){
        return Arrays.stream(this.duesGuildDatumArrays).filter(duesGuild -> duesGuild.getInventorySlot() == inventorySlot).findFirst().orElse(null);
    }
}
