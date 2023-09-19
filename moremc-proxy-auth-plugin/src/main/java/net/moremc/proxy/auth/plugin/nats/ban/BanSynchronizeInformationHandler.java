package net.moremc.proxy.auth.plugin.nats.ban;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.ban.Ban;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.ban.sync.BanSynchronizeInformationPacket;
import net.moremc.api.service.entity.BanService;


public class BanSynchronizeInformationHandler extends PacketMessengerHandler<BanSynchronizeInformationPacket> {

    private final BanService banService = API.getInstance().getBanService();

    public BanSynchronizeInformationHandler() {
        super(BanSynchronizeInformationPacket.class, "moremc_ban_channel");
    }

    @Override
    public void onHandle(BanSynchronizeInformationPacket packet) {
        switch (packet.getSynchronizeType()) {
            case REMOVE: {
                banService.findByValueOptional(packet.getNickName()).ifPresent(bazaar -> {
                    banService.deleteByValue(packet.getNickName());
                });
                break;
            }
            case CREATE: {
                Ban ban = new Gson().fromJson(packet.getBanGsonSerializer(), Ban.class);
                banService.getBanList().add(ban);
                break;
            }
        }
    }
}
