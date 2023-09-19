package net.moremc.master.controller.nats.kit.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.kit.request.KitLoadRequestPacket;
import net.moremc.api.nats.packet.kit.response.KitLoadResponsePacket;
import net.moremc.api.service.entity.KitService;

import java.util.ArrayList;

public class KitLoadRequestHandler extends PacketMessengerHandler<KitLoadRequestPacket> {

    private final KitService kitService = API.getInstance().getKitService();

    public KitLoadRequestHandler() {
        super(KitLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(KitLoadRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new KitLoadResponsePacket(new Gson().toJson(new ArrayList<>(this.kitService.getMap().values()))));
    }
}
