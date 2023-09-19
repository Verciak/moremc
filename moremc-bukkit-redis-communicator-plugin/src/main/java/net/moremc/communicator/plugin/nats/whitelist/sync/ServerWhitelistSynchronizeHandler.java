
package net.moremc.communicator.plugin.nats.whitelist.sync;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.server.Server;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.whitelist.sync.ServerWhitelistSynchronizePacket;
import net.moremc.api.service.entity.ServerService;

public class ServerWhitelistSynchronizeHandler extends PacketMessengerHandler<ServerWhitelistSynchronizePacket> {


    private final ServerService serverService = API.getInstance().getServerService();

    public ServerWhitelistSynchronizeHandler() {
        super(ServerWhitelistSynchronizePacket.class, "moremc_server_channel");
    }

    @Override
    public void onHandle(ServerWhitelistSynchronizePacket packet) {
        switch (packet.getSynchronizeType()){
            case UPDATE:{
                this.serverService.findByValueOptional(packet.getId()).ifPresent(server -> {

                    Server serverUpdate = new Gson().fromJson(packet.getSerializedServer(), Server.class);
                    this.serverService.getMap().remove(packet.getId(), server);
                    this.serverService.getMap().put(serverUpdate.getId(), serverUpdate);

                });
                break;
            }
            case CREATE:{
                Server server = new Gson().fromJson(packet.getSerializedServer(), Server.class);
                this.serverService.getMap().put(server.getId(), server);

                break;
            }
        }
    }
}
