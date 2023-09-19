package net.moremc.guilds.event.guild.region;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.bukkit.api.event.CallbackEvent;

public class GuildRegionBlockPlaceEvent implements CallbackEvent<GuildRegionBlockPlaceEvent> {

    private final Player player;
    private final GuildImpl guild;
    private final BlockPlaceEvent blockPlaceEvent;

    public GuildRegionBlockPlaceEvent(Player player, GuildImpl guild, BlockPlaceEvent event) {
        this.player = player;
        this.guild = guild;
        this.blockPlaceEvent = event;
    }

    public BlockPlaceEvent getBlockPlaceEvent() {
        return blockPlaceEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public GuildImpl getGuild() {
        return guild;
    }

    @Override
    public GuildRegionBlockPlaceEvent getEvent() {
        return this;
    }
}