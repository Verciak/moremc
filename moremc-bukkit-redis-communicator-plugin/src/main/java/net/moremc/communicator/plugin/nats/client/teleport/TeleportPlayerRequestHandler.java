package net.moremc.communicator.plugin.nats.client.teleport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.teleport.TeleportPlayerInformationPacket;
import net.moremc.api.nats.packet.teleport.TeleportPlayerRequestPacket;

public class TeleportPlayerRequestHandler extends PacketMessengerHandler<TeleportPlayerRequestPacket> {


    public TeleportPlayerRequestHandler() {
        super(TeleportPlayerRequestPacket.class, "moremc_client_channel");
    }

    @Override
    public void onHandle(TeleportPlayerRequestPacket packet) {
        Player player = Bukkit.getPlayerExact(packet.getNickNameTeleport());
        if (player == null) return;
        TeleportPlayerInformationPacket teleportPlayerInformationPacket = new TeleportPlayerInformationPacket();
        teleportPlayerInformationPacket.setTeleportDelay(packet.getTimeDelay());
        teleportPlayerInformationPacket.setNickNameTargetTo(packet.getNickNameTargetTo());
        teleportPlayerInformationPacket.setWorld(player.getWorld().getName());
        teleportPlayerInformationPacket.setX(player.getLocation().getBlockX());
        teleportPlayerInformationPacket.setY(player.getLocation().getBlockY());
        teleportPlayerInformationPacket.setZ(player.getLocation().getBlockZ());
        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", teleportPlayerInformationPacket);
    }
}
