package net.moremc.api.nats.packet.account.request;

import net.moremc.api.nats.packet.server.InfoPacket;

public class AccountLoadRequestPacket extends InfoPacket {

    private final String proxySender;

    public AccountLoadRequestPacket(String proxySender) {
        this.proxySender = proxySender;
    }

    public String getProxySender() {
        return proxySender;
    }
}
