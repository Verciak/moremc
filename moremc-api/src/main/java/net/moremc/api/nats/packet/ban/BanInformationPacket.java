package net.moremc.api.nats.packet.ban;

import net.moremc.api.nats.packet.server.InfoPacket;

public class BanInformationPacket extends InfoPacket {

    private final String nickName;
    private final String banGsonSerializer;

    public BanInformationPacket(String nickName, String banGsonSerializer){
        this.nickName = nickName;
        this.banGsonSerializer = banGsonSerializer;
    }

    public String getNickName() {
        return nickName;
    }

    public String getBanGsonSerializer() {
        return banGsonSerializer;
    }

    @Override
    public String toString() {
        return "BanInformationPacket{" +
                "nickName='" + nickName + '\'' +
                ", banGsonSerializer=" + banGsonSerializer +
                '}';
    }
}
