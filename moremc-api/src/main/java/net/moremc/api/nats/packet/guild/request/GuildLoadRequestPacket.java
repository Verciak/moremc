package net.moremc.api.nats.packet.guild.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class GuildLoadRequestPacket extends RequestPacket {


    private final String sectorSender;

    public GuildLoadRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }
}
