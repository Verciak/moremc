package net.moremc.master.controller.nats.guild.synchronize;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.repository.GuildRepository;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.GuildSynchronizePacket;
import net.moremc.api.service.entity.GuildService;

public class GuildSynchronizeHandler extends PacketMessengerHandler<GuildSynchronizePacket> {

    private final GuildRepository guildRepository = API.getInstance().getGuildRepository();
    private final GuildService guildService = API.getInstance().getGuildService();


    public GuildSynchronizeHandler() {
        super(GuildSynchronizePacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(GuildSynchronizePacket packet) {
        switch (packet.getType()){
            case CREATE: {
                GuildImpl guild = new Gson().fromJson(packet.getGuildSerialized(), GuildImpl.class);
                if (this.guildService.findByValueOptional(packet.getName()).isPresent()) return;
                this.guildService.getMap().put(guild.getName(), guild);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_guilds_channel", new GuildSynchronizePacket(guild.getName(), new Gson().toJson(guild), packet.getType()));
                this.guildRepository.create(guild.getName(), guild);
                break;
            }
            case UPDATE:{
                this.guildService.findByValueOptional(packet.getName()).ifPresent(guild -> {
                    GuildImpl guildPacket = new Gson().fromJson(packet.getGuildSerialized(), GuildImpl.class);
                    this.guildService.getMap().replace(guild.getName(), guild, guildPacket);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_guilds_channel", new GuildSynchronizePacket(guild.getName(), new Gson().toJson(guildPacket), packet.getType()));
                    this.guildRepository.update(guildPacket.getName(), guildPacket);
                });
                break;
            }
            case REMOVE:{
                this.guildService.findByValueOptional(packet.getName()).ifPresent(guild -> {
                    this.guildService.getMap().remove(guild.getName(), guild);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_guilds_channel", new GuildSynchronizePacket(guild.getName(), new Gson().toJson(guild), packet.getType()));
                    this.guildRepository.create(guild.getName(), guild);
                });
                break;
            }
        }
    }
}
