package net.moremc.proxy.auth.plugin.nats.player;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.player.PlayerKickPacket;
import net.moremc.proxy.auth.plugin.AuthPlugin;

import java.util.Optional;

public class PlayerKickHandler extends PacketMessengerHandler<PlayerKickPacket>
{
    public PlayerKickHandler() {
        super(PlayerKickPacket.class, "moremc_proxies_" + API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(PlayerKickPacket packet) {
        Optional<ProxiedPlayer> player = Optional.ofNullable(AuthPlugin.getInstance().getProxy().getPlayer(packet.getNick()));

        if(!player.isPresent()) {
            return;
        }
        player.get().disconnect(new TextComponent(packet.getReason()));
    }
}
