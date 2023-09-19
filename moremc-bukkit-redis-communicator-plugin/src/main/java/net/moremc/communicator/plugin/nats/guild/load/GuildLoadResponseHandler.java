package net.moremc.communicator.plugin.nats.guild.load;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.load.GuildLoadResponsePacket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GuildLoadResponseHandler extends PacketMessengerHandler<GuildLoadResponsePacket> {

    public GuildLoadResponseHandler() {
        super(GuildLoadResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(GuildLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<GuildImpl>>() {}.getType();
        List<GuildImpl> guildList = new Gson().fromJson(packet.getSerializedGuildList(), listOfMyClassObject);
        System.out.println("[MASTER-SERVER] Sent " + guildList.size() + " guild.");
        guildList.forEach(guild -> API.getInstance().getGuildService().getMap().put(guild.getName(), guild));
    }
}
