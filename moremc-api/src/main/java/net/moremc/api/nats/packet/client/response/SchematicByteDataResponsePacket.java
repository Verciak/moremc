package net.moremc.api.nats.packet.client.response;

import net.moremc.api.nats.packet.server.ResponsePacket;

import java.util.Map;

public class SchematicByteDataResponsePacket extends ResponsePacket {

    private final Map<String, byte[]> schematicDataCompare;

    public SchematicByteDataResponsePacket(Map<String, byte[]> schematicDataCompare) {
        this.schematicDataCompare = schematicDataCompare;
    }

    public Map<String, byte[]> getSchematicDataCompare() {
        return schematicDataCompare;
    }
}
