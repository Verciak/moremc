package net.moremc.api.nats.packet.ban.load;

import net.moremc.api.nats.packet.server.ResponsePacket;

public class BanLoadResponsePacket extends ResponsePacket {

    private final String serializedList;

    public BanLoadResponsePacket(String serializedList) {
        this.serializedList = serializedList;
    }

    public String getSerializedList() {
        return serializedList;
    }
}
