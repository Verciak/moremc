package net.moremc.api.nats.packet.guild.response;

import net.moremc.api.nats.packet.server.ResponsePacket;

public class GuildLoadResponsePacket extends ResponsePacket {

    private final String guildMapSerialized;

    public GuildLoadResponsePacket(String guildMapSerialized){
        this.guildMapSerialized = guildMapSerialized;
    }

    public String getGuildMapSerialized() {
        return guildMapSerialized;
    }

    @Override
    public String toString() {
        return "GuildLoadResponsePacket{" +
                "guildMapSerialized='" + guildMapSerialized + '\'' +
                '}';
    }
}
