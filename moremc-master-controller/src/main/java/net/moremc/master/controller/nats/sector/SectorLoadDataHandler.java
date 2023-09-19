package net.moremc.master.controller.nats.sector;

import net.moremc.api.API;
import net.moremc.api.data.SectorData;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.sector.request.SectorLoadDataRequestPacket;
import net.moremc.api.nats.packet.sector.SectorLoadDataResponsePacket;
import net.moremc.master.controller.MasterServerController;

public class SectorLoadDataHandler extends PacketMessengerHandler<SectorLoadDataRequestPacket>
{

    private final SectorData[] sectorData = MasterServerController.getInstance().getSectorData();

    public SectorLoadDataHandler() {
        super(SectorLoadDataRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(SectorLoadDataRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new SectorLoadDataResponsePacket(this.sectorData));
        System.out.println("[" + packet.getSectorSender().toUpperCase() + "-CONTROLLER] Sent a request for sectorData.");
    }
}
