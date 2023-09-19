package net.moremc.sectors.event;

import net.moremc.api.entity.user.User;
import net.moremc.bukkit.api.event.CallbackEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

public class UserBlockPlaceEvent implements CallbackEvent<UserBlockPlaceEvent> {


    private final Optional<User> optionalUser;
    private final BlockPlaceEvent blockPlaceEvent;

    public UserBlockPlaceEvent(Optional<User> optionalUser, BlockPlaceEvent blockPlaceEvent) {
        this.optionalUser = optionalUser;
        this.blockPlaceEvent = blockPlaceEvent;
    }

    public Optional<User> getOptionalUser() {
        return optionalUser;
    }

    public BlockPlaceEvent getBlockPlaceEvent() {
        return blockPlaceEvent;
    }

    @Override
    public UserBlockPlaceEvent getEvent() {
        return this;
    }

}
