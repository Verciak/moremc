package net.moremc.api.nats.packet.client.player;

import net.moremc.api.nats.packet.server.InfoPacket;

public class PlayerItemShopPacket extends InfoPacket
{
    private String name;
    private String command;

    public PlayerItemShopPacket(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }
}
