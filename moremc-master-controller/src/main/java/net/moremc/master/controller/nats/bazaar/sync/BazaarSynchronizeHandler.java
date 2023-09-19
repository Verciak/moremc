package net.moremc.master.controller.nats.bazaar.sync;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.entity.repository.BazaarRepository;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.bazaar.BazaarSynchronizePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.BazaarService;

public class BazaarSynchronizeHandler extends PacketMessengerHandler<BazaarSynchronizePacket> {


    private final BazaarService bazaarService = API.getInstance().getBazaarService();
    private final BazaarRepository bazaarRepository = API.getInstance().getBazaarRepository();

    public BazaarSynchronizeHandler() {
        super(BazaarSynchronizePacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(BazaarSynchronizePacket packet) {
        switch (packet.getSynchronizeType()) {
            case REMOVE: {
                this.bazaarService.findByValueOptional(packet.getId()).ifPresent(bazaar -> {
                    this.bazaarService.getMap().remove(bazaar.getId(), bazaar);
                    this.bazaarRepository.delete(bazaar.getID(), bazaar);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_bazaars_channel", new BazaarSynchronizePacket(bazaar.getId(), packet.getSerializedBackup(), SynchronizeType.REMOVE));
                });
                break;
            }
            case CREATE: {
                Bazaar bazaarSender = new Gson().fromJson(packet.getSerializedBackup(), Bazaar.class);
                this.bazaarService.getMap().put(bazaarSender.getId(), bazaarSender);
                this.bazaarRepository.create(bazaarSender.getID(), bazaarSender);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_bazaars_channel", new BazaarSynchronizePacket(bazaarSender.getId(), new Gson().toJson(bazaarSender), SynchronizeType.CREATE));
                break;
            }
        }
    }
}
