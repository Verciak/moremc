package net.moremc.api.nats.packet.configuration.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class ConfigurationLoadRequestPacket extends RequestPacket {

    private final String sectorSender;

    public ConfigurationLoadRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }
}
