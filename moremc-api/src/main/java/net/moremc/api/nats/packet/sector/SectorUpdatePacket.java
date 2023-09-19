package net.moremc.api.nats.packet.sector;

import net.moremc.api.nats.packet.server.InfoPacket;

import java.util.List;

public class SectorUpdatePacket extends InfoPacket {


    private final String sectorName;
    private final double ticksPerSeconds;
    private final long latestInformation;
    private final boolean online;
    private final List<String> playerList;

    public SectorUpdatePacket(String sectorName, double ticksPerSeconds, long latestInformation, boolean online, List<String> playerList) {
        this.sectorName = sectorName;
        this.ticksPerSeconds = ticksPerSeconds;
        this.latestInformation = latestInformation;
        this.online = online;
        this.playerList = playerList;
    }

    public String getSectorName() {
        return sectorName;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public long getLatestInformation() {
        return latestInformation;
    }

    public double getTicksPerSeconds() {
        return ticksPerSeconds;
    }

    public boolean isOnline() {
        return online;
    }
}
