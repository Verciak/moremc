package net.moremc.communicator.plugin.nats.schematic;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.response.SchematicByteDataResponsePacket;
import net.moremc.bukkit.api.BukkitAPI;

import java.io.IOException;

public class SchematicByteDataResponseHandler extends PacketMessengerHandler<SchematicByteDataResponsePacket> {


    public SchematicByteDataResponseHandler() {
        super(SchematicByteDataResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(SchematicByteDataResponsePacket packet) {
        packet.getSchematicDataCompare().forEach((name, dataByte) -> {
            try {
                BukkitAPI.getInstance().getSchematicFactoryMap().get(name).reloadGuildSchematic(dataByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
