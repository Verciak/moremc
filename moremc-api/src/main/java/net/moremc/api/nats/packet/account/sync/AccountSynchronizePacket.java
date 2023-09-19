package net.moremc.api.nats.packet.account.sync;

import net.moremc.api.nats.packet.server.InfoPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;

public class AccountSynchronizePacket extends InfoPacket {

    private final String nickName;
    private final String serializedAccount;
    private final SynchronizeType updateType;

    public AccountSynchronizePacket(String nickName, String serializedAccount, SynchronizeType updateType){
        this.nickName = nickName;
        this.serializedAccount = serializedAccount;
        this.updateType = updateType;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSerializedAccount() {
        return serializedAccount;
    }

    public SynchronizeType getUpdateType() {
        return updateType;
    }
    @Override
    public String toString() {
        return "AccountSynchronizePacket{" +
                "nickName='" + nickName + '\'' +
                ", serializedAccount='" + serializedAccount + '\'' +
                ", updateType=" + updateType +
                '}';
    }
}
