package net.moremc.sectors.event;

import net.moremc.api.entity.user.User;
import net.moremc.api.sector.Sector;
import net.moremc.bukkit.api.event.CallbackEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class UserSectorChangeEvent implements CallbackEvent<UserSectorChangeEvent> {


    private final Player player;
    private final Optional<User> optionalUser;
    private Sector sector;
    private final Location locationTo;

    public UserSectorChangeEvent(Player player, Optional<User> optionalUser, Sector sector, Location locationTo) {
        this.player = player;
        this.optionalUser = optionalUser;
        this.sector = sector;
        this.locationTo = locationTo;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<User> getOptionalUser() {
        return optionalUser;
    }

    public Sector getSector() {
        return sector;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    @Override
    public UserSectorChangeEvent getEvent() {
        return this;
    }

}
