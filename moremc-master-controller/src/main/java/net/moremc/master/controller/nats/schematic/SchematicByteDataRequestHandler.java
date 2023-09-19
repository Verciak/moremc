package net.moremc.master.controller.nats.schematic;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.request.SchematicByteDataRequestPacket;
import net.moremc.api.nats.packet.client.response.SchematicByteDataResponsePacket;
import net.moremc.master.controller.MasterServerController;

import java.util.Map;

public class SchematicByteDataRequestHandler extends PacketMessengerHandler<SchematicByteDataRequestPacket> {

    private final Map<String, byte[]> schematicDataCompare = MasterServerController.getInstance().getSchematicDataCompare();

    public SchematicByteDataRequestHandler() {
        super(SchematicByteDataRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(SchematicByteDataRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(),
                new SchematicByteDataResponsePacket(this.schematicDataCompare));
    }
}
