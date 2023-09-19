package net.moremc.master.controller.nats.ban.load;


import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.ban.load.BanInformationLoadPacket;
import net.moremc.api.nats.packet.ban.load.BanInformationLoadRequestPacket;
import net.moremc.api.service.entity.BanService;

public class BanInformationLoadRequestHandler extends PacketMessengerHandler<BanInformationLoadRequestPacket>
{
    private final BanService banService = API.getInstance().getBanService();

    public BanInformationLoadRequestHandler() {
        super(BanInformationLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(BanInformationLoadRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new BanInformationLoadPacket(this.banService.getBanList()));
    }
}
