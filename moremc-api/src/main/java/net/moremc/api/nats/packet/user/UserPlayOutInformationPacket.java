package net.moremc.api.nats.packet.user;


import net.moremc.api.nats.packet.server.InfoPacket;

public class UserPlayOutInformationPacket extends InfoPacket {

    private final String nickName;
    private final String userSerialized;

    public UserPlayOutInformationPacket(String nickName, String userSerialized){
        this.nickName = nickName;
        this.userSerialized = userSerialized;
    }

    public String getUserSerialized() {
        return userSerialized;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public String toString() {
        return "UserPlayOutInformationPacket{" +
                "nickName='" + nickName + '\'' +
                ", userSerialized='" + userSerialized + '\'' +
                '}';
    }
}
