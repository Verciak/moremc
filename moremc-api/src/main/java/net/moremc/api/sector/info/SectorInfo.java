package net.moremc.api.sector.info;

import net.moremc.api.sector.type.SectorType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SectorInfo implements Serializable {

    private final SectorType type;
    private double ticksPerSeconds;
    private long latestInformation;
    private boolean online;
    private List<String> playerList;
    private final SectorLocationInfo locationInfo;

    public SectorInfo(SectorType type, SectorLocationInfo locationInfo) {
        this.type = type;
        this.ticksPerSeconds = 00.00;
        this.latestInformation = 0L;
        this.online = false;
        this.playerList = new ArrayList<>();
        this.locationInfo = locationInfo;
    }

    public SectorLocationInfo getLocationInfo() {
        return locationInfo;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public SectorType getType() {
        return type;
    }

    public double getTicksPerSeconds() {
        return ticksPerSeconds;
    }

    public long getLatestInformation() {
        return latestInformation;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setLatestInformation(long latestInformation) {
        this.latestInformation = latestInformation;
    }

    public void setTicksPerSeconds(double ticksPerSeconds) {
        this.ticksPerSeconds = ticksPerSeconds;
    }
    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }

}
