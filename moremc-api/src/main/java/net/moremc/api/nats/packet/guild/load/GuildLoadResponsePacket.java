package net.moremc.api.nats.packet.guild.load;

import net.moremc.api.nats.packet.server.callback.CallbackPacket;

public class GuildLoadResponsePacket extends CallbackPacket {

    private final String serializedGuildList;

    public GuildLoadResponsePacket(String serializedGuildList) {
        this.serializedGuildList = serializedGuildList;
    }

    public String getSerializedGuildList() {
        return serializedGuildList;
    }

    @Override
    public String toString() {
        return "GuildLoadRResponsePacket{" +
                "serializedGuildList='" + serializedGuildList + '\'' +
                '}';
    }
}
