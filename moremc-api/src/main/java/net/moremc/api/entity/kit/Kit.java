package net.moremc.api.entity.kit;

import com.google.gson.Gson;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.kit.KitSynchronizePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.mysql.Identifiable;

import java.io.Serializable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Kit implements Serializable, Identifiable<String> {

    private final String name;
    private String inventoryName;
    private int delayTime;
    private String materialInventory;
    private int inventorySlot;
    private UserGroupType permissionType;
    private String inventoryGlassSerialized;
    private String inventoryItemsSerialized;

    public Kit(String name, String inventoryName, int delayTime, int inventorySlot){
        this.name = name;
        this.inventoryName = inventoryName;
        this.delayTime = delayTime;
        this.inventorySlot = inventorySlot;
        this.materialInventory = "DIAMOND";
        this.permissionType = UserGroupType.PLAYER;
        this.inventoryGlassSerialized = "null";
        this.inventoryItemsSerialized = "null";
        this.synchronize(SynchronizeType.CREATE);
    }


    public void synchronize(SynchronizeType type){
        new ScheduledThreadPoolExecutor(1).schedule(() -> {
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new KitSynchronizePacket(this.name, new Gson().toJson(this), type));
        },5L, TimeUnit.MILLISECONDS);
    }

    public String getName() {
        return name;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getMaterialInventory() {
        return materialInventory;
    }

    public String getInventoryGlassSerialized() {
        return inventoryGlassSerialized;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public String getInventoryItemsSerialized() {
        return inventoryItemsSerialized;
    }

    public UserGroupType getPermissionType() {
        return permissionType;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setInventoryGlassSerialized(String inventoryGlassSerialized) {
        this.inventoryGlassSerialized = inventoryGlassSerialized;
    }

    public void setInventoryItemsSerialized(String inventoryItemsSerialized) {
        this.inventoryItemsSerialized = inventoryItemsSerialized;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public void setInventorySlot(int inventorySlot) {
        this.inventorySlot = inventorySlot;
    }

    public void setMaterialInventory(String materialInventory) {
        this.materialInventory = materialInventory;
    }

    public void setPermissionType(UserGroupType permissionType) {
        this.permissionType = permissionType;
    }

    @Override
    public String getID() {
        return this.name;
    }
}
