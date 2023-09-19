package net.moremc.proxy.auth.plugin.nats.controller;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.controller.MasterUpdatePacket;
import net.moremc.api.service.entity.MasterService;

public class MasterUpdateHandler extends PacketMessengerHandler<MasterUpdatePacket>
{
    private final MasterService masterService = API.getInstance().getMasterService();

    public MasterUpdateHandler() {
        super(MasterUpdatePacket.class, "moremc_master_controller_update");
    }

    @Override
    public void onHandle(MasterUpdatePacket packet) {
        masterService.updateLastHeartbeat();
    }
}
