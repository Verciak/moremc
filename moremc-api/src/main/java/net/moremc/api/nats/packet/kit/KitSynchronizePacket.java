package net.moremc.api.nats.packet.kit;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.server.InfoPacket;

public class KitSynchronizePacket extends InfoPacket {

    private final String name;
    private final String kitSerialized;
    private final SynchronizeType type;

    public KitSynchronizePacket(String name, String kitSerialized, SynchronizeType type){
        this.name = name;
        this.kitSerialized = kitSerialized;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public SynchronizeType getType() {
        return type;
    }

    public String getKitSerialized() {
        return kitSerialized;
    }

    @Override
    public String toString() {
        return "KitSynchronizePacket{" +
                "name='" + name + '\'' +
                ", kitSerialized='" + kitSerialized + '\'' +
                ", type=" + type +
                '}';
    }
}
