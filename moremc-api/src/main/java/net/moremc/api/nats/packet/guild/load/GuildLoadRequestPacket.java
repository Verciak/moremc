package net.moremc.api.nats.packet.guild.load;

import net.moremc.api.nats.packet.server.callback.CallbackPacket;

public class GuildLoadRequestPacket extends CallbackPacket {

    private final String sectorSender;

    public GuildLoadRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "GuildLoadRequestPacket{" +
                "sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
