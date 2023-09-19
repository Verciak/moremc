package net.moremc.proxy.auth.plugin.nats.sector.load;

import net.moremc.api.API;
import net.moremc.api.data.SectorData;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.sector.SectorLoadDataResponsePacket;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.QueueService;
import net.moremc.proxy.auth.plugin.AuthPlugin;

import java.util.LinkedList;

public class SectorLoadDataHandler extends PacketMessengerHandler<SectorLoadDataResponsePacket>
{

    private final SectorService sectorService = API.getInstance().getSectorService();
    private final QueueService queueService = AuthPlugin.getInstance().getQueueService();

    public SectorLoadDataHandler() {
        super(SectorLoadDataResponsePacket.class, "proxy01");
    }

    @Override
    public void onHandle(SectorLoadDataResponsePacket packet) {
        for (SectorData sectorData : packet.getSectorData()) {
            this.queueService.getMap().put(sectorData.getName(), new LinkedList<>());
            this.sectorService.getMap().put(sectorData.getName(), new Sector(sectorData.getName(), sectorData.getType(), sectorData.getLocationInfo()));
        }
        System.out.println("[MoreMc-master-controller] Sent: " + packet.getSectorData().length + " sector.");
    }
}
