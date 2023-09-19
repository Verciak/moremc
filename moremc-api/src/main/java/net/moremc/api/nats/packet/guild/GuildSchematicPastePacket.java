package net.moremc.api.nats.packet.guild;

import net.moremc.api.nats.packet.server.InfoPacket;

public class GuildSchematicPastePacket extends InfoPacket {

    private final String guildName;
    private final int x;
    private final int z;


    public GuildSchematicPastePacket(String guildName, int x, int z) {
        this.guildName = guildName;
        this.x = x;
        this.z = z;
    }


    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getGuildName() {
        return guildName;
    }

    @Override
    public String toString() {
        return "GuildSchematicPastePacket{" +
                "guildName='" + guildName + '\'' +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
