package net.moremc.master.controller.nats.guild.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.GuildSynchronizePacket;
import net.moremc.api.nats.packet.guild.request.GuildPlayOutRequestPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;

public class GuildPlayOutRequestHandler extends PacketMessengerHandler<GuildPlayOutRequestPacket> {

    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildPlayOutRequestHandler() {
        super(GuildPlayOutRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(GuildPlayOutRequestPacket packet) {
        this.guildService.findByValueOptional(packet.getGuildName()).ifPresent(guild -> {
            API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new GuildSynchronizePacket(guild.getName(), new Gson().toJson(guild), SynchronizeType.UPDATE));
        });
    }
}
