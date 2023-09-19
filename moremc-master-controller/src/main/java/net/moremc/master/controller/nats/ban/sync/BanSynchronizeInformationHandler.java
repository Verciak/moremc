package net.moremc.master.controller.nats.ban.sync;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.ban.Ban;
import net.moremc.api.entity.repository.BanRepository;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.ban.sync.BanSynchronizeInformationPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.BanService;


public class BanSynchronizeInformationHandler extends PacketMessengerHandler<BanSynchronizeInformationPacket> {

    private final BanService banService = API.getInstance().getBanService();
    private final BanRepository banRepository = API.getInstance().getBanRepository();

    public BanSynchronizeInformationHandler() {
        super(BanSynchronizeInformationPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(BanSynchronizeInformationPacket packet) {
        switch (packet.getSynchronizeType()) {
            case REMOVE: {
                Ban ban = banService.findBanByNickName(packet.getNickName());
                if(ban == null) return;

                banService.getBanList().remove(ban);
                banRepository.delete(ban.getID(), ban);

                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_ban_channel", new BanSynchronizeInformationPacket(packet.getNickName(), SynchronizeType.REMOVE, packet.getBanType(), new Gson().toJson(ban)));
                break;
            }
            case CREATE: {
                Ban ban = new Gson().fromJson(packet.getBanGsonSerializer(), Ban.class);

                banService.getBanList().add(ban);
                banRepository.create(ban.getID(), ban);

                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_ban_channel",new BanSynchronizeInformationPacket(packet.getNickName(), SynchronizeType.CREATE, packet.getBanType(), new Gson().toJson(ban)));
                break;
            }
        }
    }
}
