package net.moremc.api.nats.packet.ban.load;

import net.moremc.api.entity.ban.Ban;
import net.moremc.api.nats.packet.server.InfoPacket;

import java.util.List;

public class BanInformationLoadPacket extends InfoPacket {


    private final List<Ban> banList;

    public BanInformationLoadPacket(List<Ban> banList){
        this.banList = banList;
    }

    public List<Ban> getBanList() {
        return banList;
    }

    @Override
    public String toString() {
        return "BanInformationLoadPacket{" +
                "banList=" + banList +
                '}';
    }
}
