package net.moremc.sectors.event;

import net.moremc.api.entity.user.User;
import net.moremc.bukkit.api.event.CallbackEvent;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class UserBlockBreakEvent implements CallbackEvent<UserBlockBreakEvent> {


    private final Optional<User> optionalUser;
    private final BlockBreakEvent blockBreakEvent;

    public UserBlockBreakEvent(Optional<User> optionalUser, BlockBreakEvent blockBreakEvent) {
        this.optionalUser = optionalUser;
        this.blockBreakEvent = blockBreakEvent;
    }

    public Optional<User> getOptionalUser() {
        return optionalUser;
    }

    public BlockBreakEvent getBlockBreakEvent() {
        return blockBreakEvent;
    }

    @Override
    public UserBlockBreakEvent getEvent() {
        return this;
    }

}
