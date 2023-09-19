package net.moremc.api.nats.packet.bazaar;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.server.InfoPacket;

public class BazaarSynchronizePacket extends InfoPacket {

    private final int id;
    private final String serializedBackup;
    private final SynchronizeType synchronizeType;

    public BazaarSynchronizePacket(int id, String serializedBackup, SynchronizeType synchronizeType) {
        this.id = id;
        this.serializedBackup = serializedBackup;
        this.synchronizeType = synchronizeType;
    }


    public int getId() {
        return id;
    }

    public String getSerializedBackup() {
        return serializedBackup;
    }

    public SynchronizeType getSynchronizeType() {
        return synchronizeType;
    }
    @Override
    public String toString() {
        return "BazaarSynchronizePacket{" +
                "id=" + id +
                ", serializedBackup='" + serializedBackup + '\'' +
                ", synchronizeType=" + synchronizeType +
                '}';
    }
}
