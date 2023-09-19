package net.moremc.api.entity.backpack;

import net.moremc.api.entity.backpack.type.BackPackColorType;

import java.io.Serializable;
import java.util.UUID;

public class BackPack implements Serializable
{
    private final UUID backpackUUID;

    private UUID ownerUUID;
    private final UUID firstOwnerUUID;

    private String items;
    private String description;

    private final BackPackColorType type;
    private final long createTime;


    public BackPack(UUID uuid) {
        this.backpackUUID = UUID.randomUUID();
        this.ownerUUID = uuid;
        this.firstOwnerUUID = uuid;
        this.items = "null";
        this.description = "";
        this.type = BackPackColorType.PINK;
        this.createTime = System.currentTimeMillis();
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public UUID getFirstOwnerUUID() {
        return firstOwnerUUID;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public long getCreateTime() {
        return createTime;
    }

    public UUID getBackpackUUID() {
        return backpackUUID;
    }

    public String getDescription() {
        return description;
    }

    public BackPackColorType getType() {
        return type;
    }
}
