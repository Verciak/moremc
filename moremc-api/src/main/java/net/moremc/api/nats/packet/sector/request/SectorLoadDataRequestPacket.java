package net.moremc.api.nats.packet.sector.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class SectorLoadDataRequestPacket extends RequestPacket {

    private final String sectorSender;

    public SectorLoadDataRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }
}
