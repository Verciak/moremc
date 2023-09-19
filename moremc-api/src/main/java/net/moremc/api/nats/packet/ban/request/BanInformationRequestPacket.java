package net.moremc.api.nats.packet.ban.request;

import net.moremc.api.nats.packet.server.RequestPacket;

public class BanInformationRequestPacket extends RequestPacket {

    private final String sectorSender;
    private final String nickName;

    public BanInformationRequestPacket(String sectorSender, String nickName){
        this.sectorSender = sectorSender;
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "BanInformationRequestPacket{" +
                "sectorSender='" + sectorSender + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
