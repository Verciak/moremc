package net.moremc.api.data.guild;


import net.moremc.api.data.guild.type.GuildPermissionActionDataType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuildPermissionData implements Serializable {


    private String[] inventoryLore;
    private GuildPermissionDataArray[] permissionDataArrays;

    public String[] getInventoryLore() {
        return inventoryLore;
    }

    public GuildPermissionDataArray[] getPermissionDataArrays() {
        return permissionDataArrays;
    }

    public Optional<GuildPermissionDataArray> findDataById(int id){
        return Arrays.stream(this.permissionDataArrays)
                .filter(permissionData -> permissionData.getId() == id)
                .findFirst();
    }

    public List<GuildPermissionDataArray> getGuildListPermissionBlockBreak(){
        return Arrays.stream(this.permissionDataArrays)
                .filter(permissionDataArray -> permissionDataArray.getActionType().equals(GuildPermissionActionDataType.BREAK))
                .filter(permissionDataArray -> permissionDataArray.getActionType().equals(GuildPermissionActionDataType.BREAK_PLACE))
                .collect(Collectors.toList());
    }
    public List<GuildPermissionDataArray> getGuildListPermissionBlockPlace(){
        return Arrays.stream(this.permissionDataArrays)
                .filter(permissionDataArray -> permissionDataArray.getActionType().equals(GuildPermissionActionDataType.PLACE))
                .filter(permissionDataArray -> permissionDataArray.getActionType().equals(GuildPermissionActionDataType.BREAK_PLACE))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "GuildPermissionData{" +
                "inventoryLore=" + Arrays.toString(inventoryLore) +
                ", permissionDataArrays=" + Arrays.toString(permissionDataArrays) +
                '}';
    }
}
