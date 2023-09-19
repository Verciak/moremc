package net.moremc.api.nats.packet.client.player;

import net.moremc.api.nats.packet.server.InfoPacket;


public class PlayerKickPacket extends InfoPacket
{
    private String nick;
    private String reason;

    public PlayerKickPacket(String nick, String reason) {
        this.nick = nick;
        this.reason = reason;
    }

    public String getNick() {
        return nick;
    }

    public String getReason() {
        return reason;
    }
}
