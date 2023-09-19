package net.moremc.api.nats.packet.client.player;

import net.moremc.api.nats.packet.server.ResponsePacket;

public class PlayerGlobalMessagePacket extends ResponsePacket
{
    private final String message;
    private final String permission;

    public PlayerGlobalMessagePacket(String message, String permission) {
        this.message = message;
        this.permission = permission;
    }

    public String getMessage() {
        return message;
    }

    public String getPermission() {
        return permission;
    }
}
