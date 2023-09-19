package net.moremc.api.nats.packet.bazaar.load;

import net.moremc.api.nats.packet.server.ResponsePacket;

public class BazaarLoadResponsePacket extends ResponsePacket {

    private final String serializedList;

    public BazaarLoadResponsePacket(String serializedList) {
        this.serializedList = serializedList;
    }

    public String getSerializedList() {
        return serializedList;
    }
}
