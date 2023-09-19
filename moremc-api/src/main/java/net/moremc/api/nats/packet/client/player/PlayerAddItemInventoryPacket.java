package net.moremc.api.nats.packet.client.player;

import net.moremc.api.nats.packet.server.InfoPacket;

public class PlayerAddItemInventoryPacket extends InfoPacket {

    private final String nickName;
    private final String serializedItem;

    public PlayerAddItemInventoryPacket(String nickName, String serializedItem) {
        this.nickName = nickName;
        this.serializedItem = serializedItem;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSerializedItem() {
        return serializedItem;
    }

    @Override
    public String toString() {
        return "PlayerClientAddItemInventoryPacket{" +
                "nickName='" + nickName + '\'' +
                ", serializedItem='" + serializedItem + '\'' +
                '}';
    }
}
