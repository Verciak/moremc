package net.moremc.communicator.plugin.nats.guild.synchronize;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.GuildSynchronizePacket;
import net.moremc.api.service.entity.GuildService;

public class GuildSynchronizeHandler extends PacketMessengerHandler<GuildSynchronizePacket> {

    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildSynchronizeHandler() {
        super(GuildSynchronizePacket.class, "moremc_guilds_channel");
    }

    @Override
    public void onHandle(GuildSynchronizePacket packet) {
        switch (packet.getType()){
            case CREATE: {
                GuildImpl guild = new Gson().fromJson(packet.getGuildSerialized(), GuildImpl.class);
                if (this.guildService.findByValueOptional(packet.getName()).isPresent()) return;
                this.guildService.getMap().put(guild.getName(), guild);
                break;
            }
            case UPDATE:{
                this.guildService.findByValueOptional(packet.getName()).ifPresent(guild -> {
                    GuildImpl guildPacket = new Gson().fromJson(packet.getGuildSerialized(), GuildImpl.class);
                    this.guildService.getMap().replace(guild.getName(), guild, guildPacket);
                });
                break;
            }
            case REMOVE:{
                this.guildService.findByValueOptional(packet.getName()).ifPresent(guild -> {
                    this.guildService.getMap().remove(guild.getName(), guild);
                });
                break;
            }
        }
    }
}