package net.moremc.master.controller.nats.guild.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.request.GuildLoadRequestPacket;
import net.moremc.api.nats.packet.guild.response.GuildLoadResponsePacket;
import net.moremc.api.service.entity.GuildService;

import java.util.ArrayList;

public class GuildLoadRequestHandler extends PacketMessengerHandler<GuildLoadRequestPacket> {

    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildLoadRequestHandler() {
        super(GuildLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(GuildLoadRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new GuildLoadResponsePacket(new Gson().toJson(new ArrayList<>(this.guildService.getMap().values()))));
    }
}
