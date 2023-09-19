package net.moremc.api.nats.packet.guild;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.server.InfoPacket;

public class GuildSynchronizePacket extends InfoPacket {


    private final String name;
    private final String guildSerialized;
    private final SynchronizeType type;


    public GuildSynchronizePacket(String name, String guildSerialized, SynchronizeType type){
        this.name = name;
        this.guildSerialized = guildSerialized;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getGuildSerialized() {
        return guildSerialized;
    }

    public SynchronizeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "GuildSynchronizePacket{" +
                "name='" + name + '\'' +
                ", guildSerialized='" + guildSerialized + '\'' +
                ", type=" + type +
                '}';
    }
}
