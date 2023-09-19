package net.moremc.master.controller.nats.whitelist.load;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.whitelist.load.ServerWhitelistLoadRequestPacket;
import net.moremc.api.nats.packet.whitelist.load.ServerWhitelistLoadResponsePacket;
import net.moremc.api.service.entity.ServerService;

public class ServerWhitelistLoadRequestHandler extends PacketMessengerHandler<ServerWhitelistLoadRequestPacket> {


    private final ServerService serverService = API.getInstance().getServerService();

    public ServerWhitelistLoadRequestHandler() {
        super(ServerWhitelistLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(ServerWhitelistLoadRequestPacket packet) {
        this.serverService.findByValueOptional(1).ifPresent(server -> {
            API.getInstance().getNatsMessengerAPI().sendPacket(packet.getChannelSender(), new ServerWhitelistLoadResponsePacket(new Gson().toJson(server)));
        });
    }
}
