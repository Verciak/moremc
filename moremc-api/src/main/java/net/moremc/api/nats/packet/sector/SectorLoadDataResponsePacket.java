package net.moremc.api.nats.packet.sector;

import net.moremc.api.data.SectorData;
import net.moremc.api.nats.packet.server.ResponsePacket;

public class SectorLoadDataResponsePacket extends ResponsePacket {


    private final SectorData[] sectorData;

    public SectorLoadDataResponsePacket(SectorData[] sectorData) {
        this.sectorData = sectorData;
    }

    public SectorData[] getSectorData() {
        return sectorData;
    }
}
