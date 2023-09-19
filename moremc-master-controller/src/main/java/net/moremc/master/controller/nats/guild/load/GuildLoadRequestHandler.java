package net.moremc.master.controller.nats.guild.load;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.load.GuildLoadRequestPacket;
import net.moremc.api.nats.packet.guild.load.GuildLoadResponsePacket;
import net.moremc.api.service.entity.GuildService;

import java.util.ArrayList;
import java.util.List;

public class GuildLoadRequestHandler extends PacketMessengerHandler<GuildLoadRequestPacket> {


    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildLoadRequestHandler() {
        super(GuildLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(GuildLoadRequestPacket packet) {
        List<GuildImpl> guildList = new ArrayList<>(this.guildService.getMap().values());
        GuildLoadResponsePacket guildLoadResponsePacket = new GuildLoadResponsePacket(new Gson().toJson(guildList));
        guildLoadResponsePacket.setResponse(false);
        guildLoadResponsePacket.setCallbackId(packet.getCallbackId());
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), guildLoadResponsePacket);
    }
}
