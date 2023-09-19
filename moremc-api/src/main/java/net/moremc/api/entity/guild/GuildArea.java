package net.moremc.api.entity.guild;

import java.io.Serializable;

public class GuildArea implements Serializable {

    private final int id;
    private final int x;
    private final int z;
    private boolean active;

    public GuildArea(int id, int x, int z){
        this.id = id;
        this.x = x;
        this.z = z;
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }
}
