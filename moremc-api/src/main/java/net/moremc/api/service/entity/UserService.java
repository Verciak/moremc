package net.moremc.api.service.entity;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.user.UserSynchronizeTransferPacket;
import net.moremc.api.service.ServiceImpl;

import java.util.Optional;
import java.util.UUID;

public class UserService extends ServiceImpl<String, User> {

    public void synchronizeUser(SynchronizeType synchronizeType, User user){
        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new UserSynchronizeTransferPacket(user.getNickName(), new Gson().toJson(user), synchronizeType));
    }

    public Optional<User> findUserByUUID(UUID uniqueId) {
        return this.getMap()
                .values()
                .stream()
                .filter(user -> user.getUuid().equals(uniqueId))
                .findFirst();
    }

    public Optional<User> findUserByDiscordAuthorID(String authorId){
        return this.getMap().values()
                .stream().filter(user -> user.getDiscordAuthorId().equalsIgnoreCase(authorId))
                .findFirst();
    }
}
