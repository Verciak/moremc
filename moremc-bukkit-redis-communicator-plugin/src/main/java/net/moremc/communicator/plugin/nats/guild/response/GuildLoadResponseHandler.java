package net.moremc.communicator.plugin.nats.guild.response;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.response.GuildLoadResponsePacket;
import net.moremc.api.service.entity.GuildService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class GuildLoadResponseHandler extends PacketMessengerHandler<GuildLoadResponsePacket> {

    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildLoadResponseHandler() {
        super(GuildLoadResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(GuildLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<GuildImpl>>(){}.getType();
        List<GuildImpl> guildList = new Gson().fromJson(packet.getGuildMapSerialized(), listOfMyClassObject);
        guildList.forEach(guild -> this.guildService.getMap().put(guild.getName(), guild));
        System.out.println("[MASTER-SERVER] Sent " + this.guildService.getMap().values().size() + " guild.");
    }
}
