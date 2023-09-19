package net.moremc.communicator.plugin.nats.bazaar;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.bazaar.BazaarSynchronizePacket;
import net.moremc.api.service.entity.BazaarService;

public class BazaarSynchronizeHandler extends PacketMessengerHandler<BazaarSynchronizePacket> {


    private final BazaarService bazaarService = API.getInstance().getBazaarService();

    public BazaarSynchronizeHandler() {
        super(BazaarSynchronizePacket.class, "moremc_bazaars_channel");
    }

    @Override
    public void onHandle(BazaarSynchronizePacket packet) {
        switch (packet.getSynchronizeType()) {
            case REMOVE: {

                this.bazaarService.findByValueOptional(packet.getId()).ifPresent(bazaar -> {
                    this.bazaarService.deleteByValue(packet.getId());
                });
                break;
            }
            case CREATE: {
                Bazaar bazaarSender = new Gson().fromJson(packet.getSerializedBackup(), Bazaar.class);
                this.bazaarService.getMap().put(bazaarSender.getId(), bazaarSender);
                break;
            }
        }
    }
}
