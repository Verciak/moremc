package net.moremc.api.serializer;

import java.io.Serializable;

public class LocationSerializer implements Serializable {

    private final String world;
    private  int x;
    private final int y;
    private  int z;
    private  float yaw;
    private  float pitch;
    private int size;

    public LocationSerializer(String world, int x, int y, int z, float yaw, float pitch){
        this.world = world;
        this.x = x;
        this.y =y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    public LocationSerializer(String world, int x, int y, int z, int size){
        this.world = world;
        this.x = x;
        this.y =y;
        this.z = z;
        this.size = size;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public String getWorld() {
        return world;
    }
}
