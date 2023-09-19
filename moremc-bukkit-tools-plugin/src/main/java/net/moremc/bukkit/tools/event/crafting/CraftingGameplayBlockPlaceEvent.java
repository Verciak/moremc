package net.moremc.bukkit.tools.event.crafting;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.bukkit.api.event.CallbackEvent;

public class CraftingGameplayBlockPlaceEvent implements CallbackEvent<CraftingGameplayBlockPlaceEvent> {

    private final Player player;
    private final BlockPlaceEvent blockPlaceEvent;

    public CraftingGameplayBlockPlaceEvent(Player player, BlockPlaceEvent blockPlaceEvent) {
        this.player = player;
        this.blockPlaceEvent = blockPlaceEvent;
    }

    public BlockPlaceEvent getBlockPlaceEvent() {
        return blockPlaceEvent;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public CraftingGameplayBlockPlaceEvent getEvent() {
        return this;
    }
}
