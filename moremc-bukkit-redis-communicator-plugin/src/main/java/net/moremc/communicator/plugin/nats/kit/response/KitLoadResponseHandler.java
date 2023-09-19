package net.moremc.communicator.plugin.nats.kit.response;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.kit.response.KitLoadResponsePacket;
import net.moremc.api.service.entity.KitService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KitLoadResponseHandler extends PacketMessengerHandler<KitLoadResponsePacket> {

    private final KitService kitService = API.getInstance().getKitService();

    public KitLoadResponseHandler() {
        super(KitLoadResponsePacket.class, "moremc_kits_channel");
    }

    @Override
    public void onHandle(KitLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<Kit>>(){}.getType();
        List<Kit> kitList = new Gson().fromJson(packet.getSerializedKitList(), listOfMyClassObject);
        System.out.println("[MASTER-SERVER] Sent " + kitList.size() + " kit.");
        kitList.forEach(kit -> this.kitService.getMap().put(kit.getName(), kit));
    }
}
