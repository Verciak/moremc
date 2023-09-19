package net.moremc.master.controller.nats.kit;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.entity.repository.KitRepository;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.kit.KitSynchronizePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.KitService;

public class KitSynchronizeHandler extends PacketMessengerHandler<KitSynchronizePacket> {


    private final KitRepository kitRepository = API.getInstance().getKitRepository();
    private final KitService kitService = API.getInstance().getKitService();

    public KitSynchronizeHandler() {
        super(KitSynchronizePacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(KitSynchronizePacket packet) {
        switch (packet.getType()){
            case REMOVE:{
                this.kitService.findByValueOptional(packet.getName()).ifPresent(kit -> {
                    this.kitService.getMap().remove(kit.getName(), kit);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_kits_channel", new KitSynchronizePacket(kit.getName(), new Gson().toJson(kit), SynchronizeType.REMOVE));
                    this.kitRepository.delete(kit.getName(), kit);
                });
                break;
            }
            case UPDATE:{
                this.kitService.findByValueOptional(packet.getName()).ifPresent(kit -> {
                    Kit kitSender = new Gson().fromJson(packet.getKitSerialized(), Kit.class);
                    this.kitService.getMap().replace(kit.getName(), kit, kitSender);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_kits_channel", new KitSynchronizePacket(kitSender.getName(), new Gson().toJson(kitSender), SynchronizeType.UPDATE));
                    this.kitRepository.update(kitSender.getName(), kitSender);
                });
                break;
            }
            case CREATE:{
                Kit kitSender = new Gson().fromJson(packet.getKitSerialized(), Kit.class);
                this.kitService.getMap().put(kitSender.getName(), kitSender);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_kits_channel", new KitSynchronizePacket(kitSender.getName(), new Gson().toJson(kitSender), SynchronizeType.CREATE));
                this.kitRepository.create(kitSender.getName(), kitSender);
                break;
            }
        }
    }
}
