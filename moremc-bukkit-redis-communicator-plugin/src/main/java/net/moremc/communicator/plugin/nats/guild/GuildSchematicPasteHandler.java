package net.moremc.communicator.plugin.nats.guild;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.guild.GuildSchematicPastePacket;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class GuildSchematicPasteHandler extends PacketMessengerHandler<GuildSchematicPastePacket> {


    private final GuildService guildService = API.getInstance().getGuildService();

    public GuildSchematicPasteHandler() {
        super(GuildSchematicPastePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(GuildSchematicPastePacket packet) {
        new BukkitRunnable(){

            @Override
            public void run() {
                guildService.findByValueOptional(packet.getGuildName()).ifPresent(guild -> {
                    guild.getLocation().setX(packet.getX());
                    guild.getLocation().setZ(packet.getZ());
                    BukkitAPI.getInstance().getSchematicFactoryMap().get("obsidian").pasteGuildSchematic(new Location(Bukkit.getWorld("world"),
                            guild.getLocation().getX(), 38, guild.getLocation().getZ()));
                });
            }
        }.runTaskLater(CommunicatorPlugin.getInstance(), 10L);
    }
}
