package net.moremc.communicator.plugin.nats.sector;

import net.moremc.api.API;
import net.moremc.api.data.SectorData;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.sector.SectorLoadDataResponsePacket;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;
import net.moremc.communicator.plugin.CommunicatorPlugin;

public class SectorLoadDataHandler extends PacketMessengerHandler<SectorLoadDataResponsePacket>
{

    private final SectorService sectorService = API.getInstance().getSectorService();

    public SectorLoadDataHandler() {
        super(SectorLoadDataResponsePacket.class, CommunicatorPlugin.getInstance().getSectorName());
    }

    @Override
    public void onHandle(SectorLoadDataResponsePacket packet) {
        for (SectorData sectorData : packet.getSectorData()) {
            this.sectorService.getMap().put(sectorData.getName(), new Sector(sectorData.getName(), sectorData.getType(), sectorData.getLocationInfo()));
        }
        System.out.println("[MoreMc-master-controller] Sent: " + packet.getSectorData().length + " sector.");
    }
}
