package net.moremc.api.nats.packet.user;

import net.moremc.api.nats.packet.server.InfoPacket;

public class UserSectorUpdateInformationPacket extends InfoPacket {

    private final String nickName;
    private final String sectorName;

    public UserSectorUpdateInformationPacket(String nickName, String sectorName) {
        this.nickName = nickName;
        this.sectorName = sectorName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSectorName() {
        return sectorName;
    }

    @Override
    public String toString() {
        return "UserSectorUpdateInformationPacket{" +
                "nickName='" + nickName + '\'' +
                ", sectorName='" + sectorName + '\'' +
                '}';
    }
}
