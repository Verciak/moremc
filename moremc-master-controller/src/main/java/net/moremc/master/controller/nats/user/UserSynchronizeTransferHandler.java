package net.moremc.master.controller.nats.user;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.repository.UserRepository;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.user.UserSynchronizeTransferPacket;
import net.moremc.api.service.entity.UserService;

public class UserSynchronizeTransferHandler extends PacketMessengerHandler<UserSynchronizeTransferPacket>
{

    private final UserRepository userRepository = API.getInstance().getUserRepository();
    private final UserService userService = API.getInstance().getUserService();

    public UserSynchronizeTransferHandler() {
        super(UserSynchronizeTransferPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(UserSynchronizeTransferPacket packet) {
        switch (packet.getSynchronizeType()) {
            case UPDATE: {
                this.userService.findByValueOptional(packet.getNickName()).ifPresent(user -> {
                    User userSender = new Gson().fromJson(packet.getUserSender(), User.class);
                    this.userService.getMap().replace(user.getNickName(), user, userSender);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_users_channel", new UserSynchronizeTransferPacket(userSender.getNickName(), new Gson().toJson(userSender), packet.getSynchronizeType()));
                    this.userRepository.update(userSender.getNickName(), userSender);
                });
                break;
            }
            case CREATE: {
                if(this.userService.findByValueOptional(packet.getNickName()).isPresent())return;
                User userSender = new Gson().fromJson(packet.getUserSender(), User.class);
                this.userService.getMap().put(userSender.getNickName(), userSender);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_users_channel", new UserSynchronizeTransferPacket(userSender.getNickName(),new Gson().toJson(userSender), packet.getSynchronizeType()));
                this.userRepository.create(userSender.getNickName(), userSender);
                break;
            }
        }
    }
}
