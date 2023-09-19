package net.moremc.api.nats.packet.kit.response;

import net.moremc.api.nats.packet.server.InfoPacket;

public class KitLoadResponsePacket extends InfoPacket {

    private final String serializedKitList;

    public KitLoadResponsePacket(String serializedKitList){
        this.serializedKitList = serializedKitList;
    }

    public String getSerializedKitList() {
        return serializedKitList;
    }

    @Override
    public String toString() {
        return "KitLoadResponsePacket{" +
                "serializedKitList='" + serializedKitList + '\'' +
                '}';
    }
}
