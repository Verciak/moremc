package net.moremc.api.entity.user;

import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.serializer.LocationSerializer;

import java.io.Serializable;

public class UserHome implements Serializable {

    private final int id;
    private final UserGroupType permission;
    private final int inventorySlot;
    private LocationSerializer location;


    public UserHome(int id, UserGroupType permission, int inventorySlot) {
        this.id = id;
        this.permission = permission;
        this.inventorySlot = inventorySlot;
        this.location = new LocationSerializer("world", 0, 0, 0, 0, 0);
    }

    public boolean hasSet() {
        return this.location.getX() != 0 && this.location.getY() != 0 && this.location.getZ() != 0;
    }

    public int getId() {
        return id;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public LocationSerializer getLocation() {
        return location;
    }

    public UserGroupType getPermission() {
        return permission;
    }

    public void setLocation(User user, LocationSerializer location) {
        this.location = location;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, user);
    }
}