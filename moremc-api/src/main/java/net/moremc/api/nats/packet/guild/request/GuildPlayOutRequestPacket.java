package net.moremc.api.nats.packet.guild.request;

import net.moremc.api.nats.packet.server.InfoPacket;

public class GuildPlayOutRequestPacket extends InfoPacket {

    private final String sectorSender;
    private final String guildName;

    public GuildPlayOutRequestPacket(String sectorSender, String guildName){
        this.sectorSender = sectorSender;
        this.guildName = guildName;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    public String getGuildName() {
        return guildName;
    }

}
