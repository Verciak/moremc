package net.moremc.api.nats.packet.server.callback;

import net.moremc.api.nats.packet.Packet;

public interface Callback {

    void done(Packet packet);

    void error(String message);
}
