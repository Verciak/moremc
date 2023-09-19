package net.moremc.master.controller.nats.ban.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.ban.Ban;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.ban.BanInformationPacket;
import net.moremc.api.nats.packet.ban.request.BanInformationRequestPacket;
import net.moremc.api.service.entity.BanService;


public class BanInformationRequestHandler extends PacketMessengerHandler<BanInformationRequestPacket>
{
    private final BanService banService = API.getInstance().getBanService();

    public BanInformationRequestHandler() {
        super(BanInformationRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(BanInformationRequestPacket packet) {
        Ban ban = this.banService.findBanByNickName(packet.getNickName());
        if(ban == null)return;
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new BanInformationPacket(ban.getNickName(), new Gson().toJson(ban)));
    }
}
