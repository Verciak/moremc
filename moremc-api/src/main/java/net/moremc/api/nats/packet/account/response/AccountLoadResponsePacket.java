package net.moremc.api.nats.packet.account.response;

import net.moremc.api.nats.packet.server.InfoPacket;

public class AccountLoadResponsePacket extends InfoPacket {

    private final String serializedList;

    public AccountLoadResponsePacket(String serializedList){
        this.serializedList = serializedList;
    }

    public String getSerializedList() {
        return serializedList;
    }

    @Override
    public String toString() {
        return "AccountLoadResponsePacket{" +
                "serializedList='" + serializedList + '\'' +
                '}';
    }
}
