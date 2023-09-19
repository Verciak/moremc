package net.moremc.api.nats.packet.user.load;

import net.moremc.api.nats.packet.server.callback.CallbackPacket;

public class UserLoadRequestPacket extends CallbackPacket {

    private final String sectorSender;

    public UserLoadRequestPacket(String sectorSender) {
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "UserLoadRequestPacket{" +
                "sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
