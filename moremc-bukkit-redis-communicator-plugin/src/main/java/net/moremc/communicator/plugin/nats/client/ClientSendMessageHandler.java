package net.moremc.communicator.plugin.nats.client;

import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClientSendMessageHandler extends PacketMessengerHandler<ClientSendMessagePacket> {

    public ClientSendMessageHandler() {
        super(ClientSendMessagePacket.class, "moremc_client_channel");
    }

    @Override
    public void onHandle(ClientSendMessagePacket packet) {
        switch (packet.getMessageReceiverType()){
            case PLAYER:{
                Player player = Bukkit.getPlayerExact(packet.getNickNameTarget());
                if(player == null)return;
                switch (packet.getMessageType()){
                    case TITLE:{
                        player.sendTitle("", MessageHelper.translateText(packet.getMessage()));
                        break;
                    }
                    case ACTIONBAR:{
                         //new TextComponent(MessageHelper.translateText(packet.getMessage())));
                        break;
                    }
                    case CHAT:{
                        player.sendMessage(MessageHelper.translateText(packet.getMessage()));
                        break;
                    }
                }
                break;
            }
            case ALL:{
                switch (packet.getMessageType()){
                    case TITLE:{
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle("", MessageHelper.translateText(packet.getMessage()));
                        }
                        break;
                    }
                    case ACTIONBAR:{
                        for (Player player : Bukkit.getOnlinePlayers()) {
                         //   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageHelper.translateText(packet.getMessage())));
                        }
                        break;
                    }
                    case CHAT:{
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(MessageHelper.translateText(packet.getMessage()));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
}
