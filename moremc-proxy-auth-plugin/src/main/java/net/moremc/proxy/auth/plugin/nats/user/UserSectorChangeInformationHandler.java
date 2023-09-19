package net.moremc.proxy.auth.plugin.nats.user;

import net.moremc.proxy.auth.plugin.AuthPlugin;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.user.UserSectorChangeInformationPacket;

import java.util.Optional;

public class UserSectorChangeInformationHandler extends PacketMessengerHandler<UserSectorChangeInformationPacket> {

    public UserSectorChangeInformationHandler() {
        super(UserSectorChangeInformationPacket.class, "moremc_proxies_channel");
    }

    @Override
    public void onHandle(UserSectorChangeInformationPacket packet) {
        Optional.ofNullable(AuthPlugin.getInstance().getProxy().getPlayer(packet.getNickName())).ifPresent(player -> {
            Optional.ofNullable(AuthPlugin.getInstance().getProxy().getServerInfo(packet.getSectorSender())).ifPresent(player::connect);
        });
    }
}
