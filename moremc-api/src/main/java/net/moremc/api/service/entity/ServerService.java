package net.moremc.api.service.entity;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.server.Server;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.whitelist.sync.ServerWhitelistSynchronizePacket;
import net.moremc.api.service.ServiceImpl;

public class ServerService extends ServiceImpl<Integer, Server> {


    public void synchronize(Server server, SynchronizeType synchronizeType){
        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new ServerWhitelistSynchronizePacket(server.getId(), new Gson().toJson(server), synchronizeType));
    }
}
