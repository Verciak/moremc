package net.moremc.communicator.plugin.nats.client.player;

import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.player.PlayerGlobalMessagePacket;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.communicator.plugin.CommunicatorPlugin;

public class PlayerGlobalMessageHandler extends PacketMessengerHandler<PlayerGlobalMessagePacket> {


    public PlayerGlobalMessageHandler() {
        super(PlayerGlobalMessagePacket.class, "moremc_global_channel");
    }

    @Override
    public void onHandle(PlayerGlobalMessagePacket packet) {
        CommunicatorPlugin.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            if(player.hasPermission(packet.getPermission())) {
                player.sendMessage(MessageHelper.colored(packet.getMessage()));
            }
        });
    }
}
