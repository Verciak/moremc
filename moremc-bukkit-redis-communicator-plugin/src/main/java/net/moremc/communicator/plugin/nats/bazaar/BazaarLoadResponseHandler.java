package net.moremc.communicator.plugin.nats.bazaar;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.bazaar.load.BazaarLoadResponsePacket;
import net.moremc.api.service.entity.BazaarService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BazaarLoadResponseHandler extends PacketMessengerHandler<BazaarLoadResponsePacket> {

    private final BazaarService bazaarService = API.getInstance().getBazaarService();

    public BazaarLoadResponseHandler() {
        super(BazaarLoadResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(BazaarLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<Bazaar>>() {}.getType();
        List<Bazaar> bazaarList = new Gson().fromJson(packet.getSerializedList(), listOfMyClassObject);
        System.out.println("[MASTER-SERVER] Sent " + bazaarList.size() + " bazaar.");
        bazaarList.forEach(bazaar -> API.getInstance().getBazaarService().getMap().put(bazaar.getId(), bazaar));
    }
}
