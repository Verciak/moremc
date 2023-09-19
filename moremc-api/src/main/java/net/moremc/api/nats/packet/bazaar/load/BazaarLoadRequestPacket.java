package net.moremc.api.nats.packet.bazaar.load;

import net.moremc.api.nats.packet.server.RequestPacket;

public class BazaarLoadRequestPacket extends RequestPacket {

    private final String sectorSender;

    public BazaarLoadRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "BazaarLoadRequestPacket{" +
                "sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
