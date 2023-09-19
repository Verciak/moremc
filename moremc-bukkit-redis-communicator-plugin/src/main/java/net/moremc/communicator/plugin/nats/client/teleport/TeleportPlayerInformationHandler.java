package net.moremc.communicator.plugin.nats.client.teleport;

import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.teleport.TeleportPlayerInformationPacket;
import net.moremc.bukkit.api.helper.TeleportHelper;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportPlayerInformationHandler extends PacketMessengerHandler<TeleportPlayerInformationPacket> {


    public TeleportPlayerInformationHandler() {
        super(TeleportPlayerInformationPacket.class, "moremc_client_channel");
    }

    @Override
    public void onHandle(TeleportPlayerInformationPacket packet) {
        Player player = Bukkit.getPlayerExact(packet.getNickNameTargetTo());
        if (player == null) return;
        new BukkitRunnable(){
            @Override
            public void run() {
                TeleportHelper.teleport(player, packet.getTeleportDelay(),
                        new Location(Bukkit.getWorld(packet.getWorld()), packet.getX(), packet.getY(), packet.getZ()));
            }
        }.runTaskLater(CommunicatorPlugin.getInstance(), 2L);
    }
}
