package net.moremc.api.nats.packet.whitelist.load;

import net.moremc.api.nats.packet.server.InfoPacket;

public class ServerWhitelistLoadResponsePacket extends InfoPacket {

    private final String serializedServer;

    public ServerWhitelistLoadResponsePacket(String serializedServer) {
        this.serializedServer = serializedServer;
    }

    public String getSerializedServer() {
        return serializedServer;
    }

    @Override
    public String toString() {
        return "ServerWhitelistLoadResponsePacket{" +
                "serializedServer='" + serializedServer + '\'' +
                '}';
    }
}
