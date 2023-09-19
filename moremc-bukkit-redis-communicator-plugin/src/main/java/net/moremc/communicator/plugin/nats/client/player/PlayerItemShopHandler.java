package net.moremc.communicator.plugin.nats.client.player;

import org.bukkit.Bukkit;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.client.player.PlayerItemShopPacket;

import java.util.Optional;

public class PlayerItemShopHandler extends PacketMessengerHandler<PlayerItemShopPacket>
{
    public PlayerItemShopHandler() {
        super(PlayerItemShopPacket.class, "moremc_global_channel");
    }

    @Override
    public void onHandle(PlayerItemShopPacket packet) {
        Optional<User> user = API.getInstance().getUserService().findByValueOptional(packet.getName());

        if(!user.isPresent()) return;
        if(user.get().hasDisableMessage(14)) {
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_global_channel", new ClientSendMessagePacket(
                    "           &8&l&m-[&6&l&m---&8&m&l[--&8 &e&lMORE&f&lMC.EU &8&l&m--]&6&l&m---&8&l&m]-&8" +
                    "&8>> &fGracz: &e{PLAYER} &fzakupil usluge: &e" + packet.getName() +
                    "&8>> &fDziekujemy za wsparcie naszego serwera z: &ehttps://moremc.eu/" +
                    " "
                    , SendMessageReceiverType.ALL, SendMessageType.CHAT)
            );
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), packet.getCommand());
    }
}
