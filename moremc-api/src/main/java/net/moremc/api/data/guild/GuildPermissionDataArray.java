package net.moremc.api.data.guild;

import net.moremc.api.data.guild.type.GuildPermissionActionDataType;
import net.moremc.api.data.guild.type.GuildPermissionDataMaterialType;

import java.io.Serializable;

public class GuildPermissionDataArray implements Serializable {


    private int id;
    private String polishName;
    private String materialNameOrUrl;
    private GuildPermissionActionDataType actionType;
    private GuildPermissionDataMaterialType materialType;

    public int getId() {
        return id;
    }

    public String getMaterialNameOrUrl() {
        return materialNameOrUrl;
    }

    public GuildPermissionActionDataType getActionType() {
        return actionType;
    }

    public GuildPermissionDataMaterialType getMaterialType() {
        return materialType;
    }

    public String getPolishName() {
        return polishName;
    }

    @Override
    public String toString() {
        return "GuildPermissionDataArray{" +
                "id=" + id +
                ", polishName='" + polishName + '\'' +
                ", materialNameOrUrl='" + materialNameOrUrl + '\'' +
                ", actionType=" + actionType +
                ", materialType=" + materialType +
                '}';
    }
}
