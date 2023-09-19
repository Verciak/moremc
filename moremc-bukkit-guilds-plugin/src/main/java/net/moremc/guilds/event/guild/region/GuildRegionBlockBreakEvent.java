package net.moremc.guilds.event.guild.region;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.bukkit.api.event.CallbackEvent;

public class GuildRegionBlockBreakEvent implements CallbackEvent<GuildRegionBlockBreakEvent> {

    private final Player player;
    private final GuildImpl guild;
    private final BlockBreakEvent blockBreakEvent;

    public GuildRegionBlockBreakEvent(Player player, GuildImpl guild, BlockBreakEvent event) {
        this.player = player;
        this.guild = guild;
        this.blockBreakEvent = event;
    }

    public BlockBreakEvent getBlockBreakEvent() {
        return blockBreakEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public GuildImpl getGuild() {
        return guild;
    }

    @Override
    public GuildRegionBlockBreakEvent getEvent() {
        return this;
    }
}
