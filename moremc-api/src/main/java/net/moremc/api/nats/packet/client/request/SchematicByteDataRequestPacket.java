package net.moremc.api.nats.packet.client.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class SchematicByteDataRequestPacket extends RequestPacket {

    private final String sectorSender;


    public SchematicByteDataRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }
}
