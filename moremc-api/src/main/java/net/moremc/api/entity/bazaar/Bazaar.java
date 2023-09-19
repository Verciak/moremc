package net.moremc.api.entity.bazaar;

import com.google.gson.Gson;
import net.moremc.api.nats.packet.bazaar.BazaarSynchronizePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.mysql.Identifiable;

import java.io.Serializable;

public class Bazaar implements Serializable, Identifiable<String> {

    private final int id;
    private final String nickName;
    private final String serializedItem;
    private final int sellCount;
    private final long activeTime;

    public Bazaar(int id, String nickName, String serializedItem, int sellCount){
        this.id = id;
        this.nickName = nickName;
        this.serializedItem = serializedItem;
        this.sellCount = sellCount;
        this.activeTime = System.currentTimeMillis() + TimeType.HOUR.getTime(5);

        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new BazaarSynchronizePacket(this.id, new Gson().toJson(this), SynchronizeType.CREATE));
    }

    public String getNickName() {
        return nickName;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public int getId() {
        return id;
    }

    public int getSellCount() {
        return sellCount;
    }

    public String getSerializedItem() {
        return serializedItem;
    }

    @Override
    public String getID() {
        return String.valueOf(this.id);
    }
}
