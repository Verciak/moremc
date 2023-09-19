package net.moremc.master.controller.nats.user.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.user.UserPlayOutRequestPacket;
import net.moremc.api.nats.packet.user.UserSynchronizeTransferPacket;
import net.moremc.api.service.entity.UserService;

public class UserPlayOutRequestHandler extends PacketMessengerHandler<UserPlayOutRequestPacket> {

    private final UserService userService = API.getInstance().getUserService();

    public UserPlayOutRequestHandler() {
        super(UserPlayOutRequestPacket.class, "moremc_master_controller");
    }


    @Override
    public void onHandle(UserPlayOutRequestPacket packet) {
        this.userService.findByValueOptional(packet.getNickName()).ifPresent(user -> {
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_users_channel", new UserSynchronizeTransferPacket(user.getNickName(), new Gson().toJson(user), SynchronizeType.UPDATE));
        });
    }
}
