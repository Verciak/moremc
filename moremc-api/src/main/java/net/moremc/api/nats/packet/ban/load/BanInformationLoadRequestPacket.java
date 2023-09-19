package net.moremc.api.nats.packet.ban.load;

import net.moremc.api.nats.packet.server.RequestPacket;

public class BanInformationLoadRequestPacket extends RequestPacket {

    private final String sectorSender;

    public BanInformationLoadRequestPacket(String sectorSender){
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "BanInformationLoadRequestPacket{" +
                "sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
