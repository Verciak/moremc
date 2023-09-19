package net.moremc.api.nats.packet.whitelist.load;

import net.moremc.api.nats.packet.server.RequestPacket;

public class ServerWhitelistLoadRequestPacket extends RequestPacket {

    private final String channelSender;


    public ServerWhitelistLoadRequestPacket(String channelSender) {
        this.channelSender = channelSender;
    }

    public String getChannelSender() {
        return channelSender;
    }

    @Override
    public String toString() {
        return "ServerWhitelistLoadRequestPacket{" +
                "channelSender='" + channelSender + '\'' +
                '}';
    }
}
