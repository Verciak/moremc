package net.moremc.communicator.plugin.nats.kit;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.kit.KitSynchronizePacket;
import net.moremc.api.service.entity.KitService;

public class KitSynchronizeHandler extends PacketMessengerHandler<KitSynchronizePacket> {
    
    
    private final KitService kitService = API.getInstance().getKitService();

    public KitSynchronizeHandler() {
        super(KitSynchronizePacket.class, "moremc_kits_channel");
    }

    @Override
    public void onHandle(KitSynchronizePacket packet) {
        switch (packet.getType()){
            case REMOVE:{
                this.kitService.findByValueOptional(packet.getName()).ifPresent(kit -> {
                    this.kitService.getMap().remove(kit.getName(), kit);
                });
                break;
            }
            case UPDATE:{
                this.kitService.findByValueOptional(packet.getName()).ifPresent(kit -> {
                    Kit kitSender = new Gson().fromJson(packet.getKitSerialized(), Kit.class);
                    this.kitService.getMap().replace(kit.getName(), kit, kitSender);
                });
                break;
            }
            case CREATE:{
                Kit kitSender = new Gson().fromJson(packet.getKitSerialized(), Kit.class);
                this.kitService.getMap().put(kitSender.getName(), kitSender);
                break;
            }
        }
    }
}
