package net.moremc.master.controller.nats.bazaar.load;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.bazaar.load.BazaarLoadRequestPacket;
import net.moremc.api.nats.packet.bazaar.load.BazaarLoadResponsePacket;
import net.moremc.api.service.entity.BazaarService;

import java.util.ArrayList;

public class BazaarLoadRequestHandler extends PacketMessengerHandler<BazaarLoadRequestPacket> {


    private final BazaarService bazaarService = API.getInstance().getBazaarService();

    public BazaarLoadRequestHandler() {
        super(BazaarLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(BazaarLoadRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new BazaarLoadResponsePacket(new Gson().toJson(new ArrayList<>(this.bazaarService.getMap().values()))));
    }
}
