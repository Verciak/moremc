package net.moremc.guilds.event.guild;

import org.bukkit.entity.Player;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.bukkit.api.event.CallbackEvent;

import java.util.Optional;

public class GuildPlayerMoveEvent implements CallbackEvent<GuildPlayerMoveEvent> {

    private final Player player;
    private final Optional<GuildImpl> optionalGuildFrom;
    private final Optional<GuildImpl> optionalGuildTo;

    public GuildPlayerMoveEvent(Player player, Optional<GuildImpl> optionalGuildFrom, Optional<GuildImpl> optionalGuildTo) {
        this.player = player;
        this.optionalGuildFrom = optionalGuildFrom;
        this.optionalGuildTo = optionalGuildTo;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<GuildImpl> getOptionalGuildFrom() {
        return optionalGuildFrom;
    }

    public Optional<GuildImpl> getOptionalGuildTo() {
        return optionalGuildTo;
    }

    @Override
    public GuildPlayerMoveEvent getEvent() {
        return this;
    }
}
