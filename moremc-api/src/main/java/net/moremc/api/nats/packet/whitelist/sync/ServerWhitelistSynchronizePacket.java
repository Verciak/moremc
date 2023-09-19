package net.moremc.api.nats.packet.whitelist.sync;

import net.moremc.api.nats.packet.server.InfoPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;

public class ServerWhitelistSynchronizePacket extends InfoPacket {

    private final int id;
    private final String serializedServer;
    private final SynchronizeType synchronizeType;


    public ServerWhitelistSynchronizePacket(int id, String serializedServer, SynchronizeType synchronizeType) {
        this.id = id;
        this.serializedServer = serializedServer;
        this.synchronizeType = synchronizeType;
    }

    public SynchronizeType getSynchronizeType() {
        return synchronizeType;
    }

    public int getId() {
        return id;
    }

    public String getSerializedServer() {
        return serializedServer;
    }

    @Override
    public String toString() {
        return "ServerWhitelistSynchronizePacket{" +
                "id=" + id +
                ", serializedServer='" + serializedServer + '\'' +
                ", synchronizeType=" + synchronizeType +
                '}';
    }
}
