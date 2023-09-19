package net.moremc.communicator.plugin.nats.client.player;

import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.player.PlayerAddItemInventoryPacket;
import net.moremc.bukkit.api.serializer.ItemSerializer;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerAddItemInventoryHandler extends PacketMessengerHandler<PlayerAddItemInventoryPacket> {

    public PlayerAddItemInventoryHandler() {
        super(PlayerAddItemInventoryPacket.class, "moremc_client_channel");
    }

    @Override
    public void onHandle(PlayerAddItemInventoryPacket packet) {
        Player player = Bukkit.getPlayerExact(packet.getNickName());
        if(player == null)return;

        Bukkit.getScheduler().runTaskLater(CommunicatorPlugin.getInstance(), () -> {
            ItemStack itemStack = ItemSerializer.encodeItem(packet.getSerializedItem())[0];
            player.getInventory().addItem(itemStack);
        }, 3L);
    }
}
