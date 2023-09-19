package net.moremc.api.nats.packet.user.load;

import net.moremc.api.nats.packet.server.callback.CallbackPacket;

public class UserLoadResponsePacket extends CallbackPacket {

    private final String serializedUserList;

    public UserLoadResponsePacket(String serializedUserList) {
        this.serializedUserList = serializedUserList;
    }

    public String getSerializedUserList() {
        return serializedUserList;
    }

    @Override
    public String toString() {
        return "UserLoadResponsePacket{" +
                "serializedUserList='" + serializedUserList + '\'' +
                '}';
    }
}
