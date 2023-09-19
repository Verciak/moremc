package net.moremc.communicator.plugin.nats.client.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.player.PlayerCloseInventoryPacket;

public class PlayerCloseInventoryHandler extends PacketMessengerHandler<PlayerCloseInventoryPacket> {

    public PlayerCloseInventoryHandler() {
        super(PlayerCloseInventoryPacket.class, "moremc_client_channel");
    }

    @Override
    public void onHandle(PlayerCloseInventoryPacket packet) {
        Player player = Bukkit.getPlayerExact(packet.getNickName());
        if(player == null)return;
        player.closeInventory();
    }
}
