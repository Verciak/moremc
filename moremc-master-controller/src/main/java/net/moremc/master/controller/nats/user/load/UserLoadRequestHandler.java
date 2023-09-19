package net.moremc.master.controller.nats.user.load;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.user.load.UserLoadRequestPacket;
import net.moremc.api.nats.packet.user.load.UserLoadResponsePacket;
import net.moremc.api.service.entity.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserLoadRequestHandler extends PacketMessengerHandler<UserLoadRequestPacket> {


    private final UserService userService = API.getInstance().getUserService();

    public UserLoadRequestHandler() {
        super(UserLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(UserLoadRequestPacket packet) {
        List<User> userList = new ArrayList<>(this.userService.getMap().values());
        UserLoadResponsePacket loadResponsePacket = new UserLoadResponsePacket(new Gson().toJson(userList));
        loadResponsePacket.setResponse(false);
        loadResponsePacket.setCallbackId(packet.getCallbackId());
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), loadResponsePacket);
    }
}
