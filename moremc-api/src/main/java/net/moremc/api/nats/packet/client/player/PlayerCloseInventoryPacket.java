package net.moremc.api.nats.packet.client.player;

import net.moremc.api.nats.packet.server.InfoPacket;

public class PlayerCloseInventoryPacket extends InfoPacket {

    private final String nickName;

    public PlayerCloseInventoryPacket(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }
}
