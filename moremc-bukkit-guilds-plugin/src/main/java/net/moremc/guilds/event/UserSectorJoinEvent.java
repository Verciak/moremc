
package net.moremc.guilds.event;

import net.moremc.api.entity.user.User;
import net.moremc.api.sector.Sector;
import net.moremc.bukkit.api.event.CallbackEvent;
import org.bukkit.entity.Player;

import java.util.Optional;

public class UserSectorJoinEvent implements CallbackEvent<UserSectorJoinEvent> {

    private final Player player;
    private final Optional<User> optionalUser;
    private final Optional<Sector> optionalSector;

    public UserSectorJoinEvent(Optional<Sector> optionalSector, Optional<User> optionalUser, Player player){
        this.optionalUser = optionalUser;
        this.optionalSector = optionalSector;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<User> getOptionalUser() {
        return optionalUser;
    }


    public Optional<Sector> getOptionalSector() {
        return optionalSector;
    }

    @Override
    public UserSectorJoinEvent getEvent() {
        return this;
    }
}