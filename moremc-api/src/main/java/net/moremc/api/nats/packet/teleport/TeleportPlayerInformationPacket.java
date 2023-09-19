package net.moremc.api.nats.packet.teleport;

import net.moremc.api.nats.packet.server.InfoPacket;

public class TeleportPlayerInformationPacket extends InfoPacket {

    private String nickNameTargetTo;
    private String nickNameTeleport;
    private String world;
    private int x;
    private int y;
    private int z;
    private long teleportDelay;

    public String getNickNameTargetTo() {
        return nickNameTargetTo;
    }

    public void setNickNameTargetTo(String nickNameTargetTo) {
        this.nickNameTargetTo = nickNameTargetTo;
    }

    public String getNickNameTeleport() {
        return nickNameTeleport;
    }

    public void setNickNameTeleport(String nickNameTeleport) {
        this.nickNameTeleport = nickNameTeleport;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "TeleportPlayerInformation{" +
                "nickNameTargetTo='" + nickNameTargetTo + '\'' +
                ", nickNameTeleport='" + nickNameTeleport + '\'' +
                ", world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public long getTeleportDelay() {
        return teleportDelay;
    }

    public void setTeleportDelay(long teleportDelay) {
        this.teleportDelay = teleportDelay;
    }
}
