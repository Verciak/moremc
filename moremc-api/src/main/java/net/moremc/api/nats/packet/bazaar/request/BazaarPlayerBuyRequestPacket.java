package net.moremc.api.nats.packet.bazaar.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class BazaarPlayerBuyRequestPacket extends RequestPacket {

    private final int id;
    private final int sellAmount;
    private final String nickName;

    public BazaarPlayerBuyRequestPacket(int id, int sellAmount, String nickName){
        this.id = id;
        this.sellAmount = sellAmount;
        this.nickName = nickName;
    }
    public int getId() {
        return id;
    }
    public String getNickName() {
        return nickName;
    }
    public int getSellAmount() {
        return sellAmount;
    }

    @Override
    public String toString() {
        return "BazaarPlayerBuyRequestPacket{" +
                "id=" + id +
                ", sellAmount=" + sellAmount +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
