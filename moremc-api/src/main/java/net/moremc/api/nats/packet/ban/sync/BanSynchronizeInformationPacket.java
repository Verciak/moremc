package net.moremc.api.nats.packet.ban.sync;

import net.moremc.api.nats.packet.server.InfoPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.ban.type.BanType;

public class BanSynchronizeInformationPacket extends InfoPacket {


    private final String nickName;
    private final SynchronizeType synchronizeType;
    private final BanType banType;
    private final String banGsonSerializer;

    public BanSynchronizeInformationPacket(String nickName, SynchronizeType synchronizeType, BanType banType, String banGsonSerializer){
        this.nickName = nickName;
        this.synchronizeType =  synchronizeType;
        this.banType = banType;
        this.banGsonSerializer = banGsonSerializer;
    }

    public String getNickName() {
        return nickName;
    }

    public SynchronizeType getSynchronizeType() {
        return synchronizeType;
    }

    public String getBanGsonSerializer() {
        return banGsonSerializer;
    }

    public BanType getBanType() {
        return banType;
    }

}
