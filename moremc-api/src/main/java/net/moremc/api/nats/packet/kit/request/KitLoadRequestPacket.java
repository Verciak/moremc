package net.moremc.api.nats.packet.kit.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class KitLoadRequestPacket extends RequestPacket {

    private final String sectorSender;

    public KitLoadRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "KitLoadRequestPacket{" +
                "sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
