package net.moremc.communicator.plugin.nats.whitelist.load;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.server.Server;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.whitelist.load.ServerWhitelistLoadResponsePacket;
import net.moremc.api.service.entity.ServerService;

public class ServerWhitelistLoadResponseHandler extends PacketMessengerHandler<ServerWhitelistLoadResponsePacket> {

    private final ServerService serverService = API.getInstance().getServerService();


    public ServerWhitelistLoadResponseHandler() {
        super(ServerWhitelistLoadResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(ServerWhitelistLoadResponsePacket packet) {
        Server server = new Gson().fromJson(packet.getSerializedServer(), Server.class);
        this.serverService.getMap().put(server.getId(), server);
        System.out.println("[MASTER-SERVER] Sent " + this.serverService.getMap().size() + " server.");
    }
}
