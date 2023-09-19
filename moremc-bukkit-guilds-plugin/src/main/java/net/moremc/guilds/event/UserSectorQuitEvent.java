package net.moremc.guilds.event;

import org.bukkit.entity.Player;
import net.moremc.api.entity.user.User;
import net.moremc.bukkit.api.event.CallbackEvent;

import java.util.Optional;

public class UserSectorQuitEvent implements CallbackEvent<UserSectorQuitEvent> {


    private final Optional<User> optionalUser;
    private final Player player;

    public UserSectorQuitEvent(Optional<User> optionalUser, Player player){
        this.optionalUser = optionalUser;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<User> getOptionalUser() {
        return optionalUser;
    }

    @Override
    public UserSectorQuitEvent getEvent() {
        return this;
    }
}
