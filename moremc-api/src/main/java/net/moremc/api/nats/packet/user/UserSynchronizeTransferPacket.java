package net.moremc.api.nats.packet.user;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.server.InfoPacket;

public class UserSynchronizeTransferPacket extends InfoPacket {


    private final String nickName;
    private final String userSender;
    private final SynchronizeType synchronizeType;


    public UserSynchronizeTransferPacket(String nickName, String userSender, SynchronizeType synchronizeType){
        this.nickName = nickName;
        this.userSender = userSender;
        this.synchronizeType = synchronizeType;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserSender() {
        return userSender;
    }

    public SynchronizeType getSynchronizeType() {
        return synchronizeType;
    }

    @Override
    public String toString() {
        return "UserSynchronizeTransferPacket{" +
                "nickName='" + nickName + '\'' +
                ", userSender=" + userSender +
                ", synchronizeType=" + synchronizeType +
                '}';
    }
}
