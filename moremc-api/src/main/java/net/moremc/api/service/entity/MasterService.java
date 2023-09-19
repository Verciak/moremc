package net.moremc.api.service.entity;

public class MasterService
{

    private long lastHeartbeat;

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public boolean isConnected() {
        return lastHeartbeat + 2500L > System.currentTimeMillis();
    }

    public void updateLastHeartbeat() {
        lastHeartbeat = System.currentTimeMillis();
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
}