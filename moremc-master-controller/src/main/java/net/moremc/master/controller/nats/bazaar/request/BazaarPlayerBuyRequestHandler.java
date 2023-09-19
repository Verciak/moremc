package net.moremc.master.controller.nats.bazaar.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.bazaar.BazaarSynchronizePacket;
import net.moremc.api.nats.packet.bazaar.request.BazaarPlayerBuyRequestPacket;
import net.moremc.api.nats.packet.client.player.PlayerAddItemInventoryPacket;
import net.moremc.api.nats.packet.client.player.PlayerCloseInventoryPacket;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.BazaarService;
import net.moremc.api.service.entity.UserService;

public class BazaarPlayerBuyRequestHandler extends PacketMessengerHandler<BazaarPlayerBuyRequestPacket>
{
    private final API api = API.getInstance();

    private final BazaarService bazaarService = API.getInstance().getBazaarService();
    private final UserService userService = API.getInstance().getUserService();

    public BazaarPlayerBuyRequestHandler() {
        super(BazaarPlayerBuyRequestPacket.class, "moremc_master_controller");
    }
    @Override
    public void onHandle(BazaarPlayerBuyRequestPacket packet) {
        if(!this.bazaarService.findByValueOptional(packet.getId()).isPresent()){

            ClientSendMessagePacket message = new ClientSendMessagePacket(
                    "&cWygląda na to że ktoś cię wyprzędził i podana transakacja już nie istnieje", SendMessageReceiverType.PLAYER, SendMessageType.CHAT
            );
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", message);
            return;
        }
        this.bazaarService.findByValueOptional(packet.getId()).ifPresent(bazaar -> {
            this.userService.findByValueOptional(packet.getNickName()).ifPresent(user -> {

                if(packet.getSellAmount() < bazaar.getSellCount()){
                    ClientSendMessagePacket message = new ClientSendMessagePacket(
                            "&cWygląda na to że cię nie stać na ten przedmiot.", SendMessageReceiverType.PLAYER, SendMessageType.TITLE
                    );
                    message.setNickNameTarget(packet.getNickName());
                    api.getNatsMessengerAPI().sendPacket("moremc_client_channel", message);
                    api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new PlayerCloseInventoryPacket(user.getNickName()));
                    return;
                }
                ClientSendMessagePacket message = new ClientSendMessagePacket(
                        "&ePomyślnie &fzakupiono przedmiot &8(&fID&8:&e " + bazaar.getID() + "&7, &fCena&8: &e" + bazaar.getSellCount() + "&8)", SendMessageReceiverType.PLAYER, SendMessageType.TITLE
                );
                message.setNickNameTarget(packet.getNickName());

                api.getNatsMessengerAPI().sendPacket("moremc_master_controller", new BazaarSynchronizePacket(packet.getId(), new Gson().toJson(bazaar), SynchronizeType.REMOVE));
                api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new PlayerAddItemInventoryPacket(user.getNickName(), bazaar.getSerializedItem()));
                api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new PlayerCloseInventoryPacket(user.getNickName()));
                api.getNatsMessengerAPI().sendPacket("moremc_client_channel", message);
            });
        });
    }
}
