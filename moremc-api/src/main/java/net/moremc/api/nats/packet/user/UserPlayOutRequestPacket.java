package net.moremc.api.nats.packet.user;

import net.moremc.api.nats.packet.server.RequestPacket;

public class UserPlayOutRequestPacket extends RequestPacket {

    private final String nickName;
    private final String sectorSender;

    public UserPlayOutRequestPacket(String nickName, String sectorSender){
        this.nickName = nickName;
        this.sectorSender = sectorSender;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public String toString() {
        return "UserPlayOutRequestPacket{" +
                "nickName='" + nickName + '\'' +
                ", sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
