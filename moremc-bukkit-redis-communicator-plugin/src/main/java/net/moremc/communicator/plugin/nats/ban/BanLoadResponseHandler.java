package net.moremc.communicator.plugin.nats.ban;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.ban.Ban;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.ban.load.BanLoadResponsePacket;
import net.moremc.api.service.entity.BanService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BanLoadResponseHandler extends PacketMessengerHandler<BanLoadResponsePacket>
{

    private final BanService banService = API.getInstance().getBanService();

    public BanLoadResponseHandler() {
        super(BanLoadResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(BanLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<Ban>>() {}.getType();
        List<Ban> banList = new Gson().fromJson(packet.getSerializedList(), listOfMyClassObject);

        System.out.println("[MASTER-SERVER] Sent " + banList.size() + " bans.");
        banList.forEach(ban -> banService.getMap().put(ban.getNickName(), ban));
    }
}
