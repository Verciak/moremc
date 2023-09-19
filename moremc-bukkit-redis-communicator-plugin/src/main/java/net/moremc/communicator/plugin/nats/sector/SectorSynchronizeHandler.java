package net.moremc.communicator.plugin.nats.sector;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.sector.SectorUpdatePacket;
import net.moremc.api.service.entity.SectorService;


public class SectorSynchronizeHandler extends PacketMessengerHandler<SectorUpdatePacket>
{

    private final SectorService sectorService = API.getInstance().getSectorService();

    public SectorSynchronizeHandler() {
        super(SectorUpdatePacket.class, "moremc_sectors");
    }

    @Override
    public void onHandle(SectorUpdatePacket packet) {
        this.sectorService.findByValueOptional(packet.getSectorName()).ifPresent(sector -> {
            sector.getInfo().setOnline(packet.isOnline());
            sector.getInfo().setTicksPerSeconds(packet.getTicksPerSeconds());
            sector.getInfo().setPlayerList(packet.getPlayerList());
            sector.getInfo().setLatestInformation(packet.getLatestInformation());
        });
    }
}
