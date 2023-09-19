package net.moremc.communicator.plugin.nats.user.load;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.user.load.UserLoadResponsePacket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserLoadResponseHandler extends PacketMessengerHandler<UserLoadResponsePacket> {

    public UserLoadResponseHandler() {
        super(UserLoadResponsePacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(UserLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<User>>() {}.getType();
        List<User> userList = new Gson().fromJson(packet.getSerializedUserList(), listOfMyClassObject);
        System.out.println("[MASTER-SERVER] Sent " + userList.size() + " user.");
        userList.forEach(user -> API.getInstance().getUserService().getMap().put(user.getNickName(), user));
    }
}
