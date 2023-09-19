package net.moremc.api.nats.packet.user;

import net.moremc.api.nats.packet.server.InfoPacket;

public class UserSectorChangeInformationPacket extends InfoPacket {

    private final String nickName;
    private final String sectorSender;

    public UserSectorChangeInformationPacket(String nickName, String sectorSender) {
        this.nickName = nickName;
        this.sectorSender = sectorSender;
    }


    public String getNickName() {
        return nickName;
    }

    public String getSectorSender() {
        return sectorSender;
    }

    @Override
    public String toString() {
        return "UserSectorChangeInformationPacket{" +
                "nickName='" + nickName + '\'' +
                ", sectorSender='" + sectorSender + '\'' +
                '}';
    }
}
