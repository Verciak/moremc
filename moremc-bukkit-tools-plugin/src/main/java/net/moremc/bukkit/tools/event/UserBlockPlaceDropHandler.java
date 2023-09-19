package net.moremc.bukkit.tools.event;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.sectors.event.UserBlockPlaceEvent;

import java.util.function.Consumer;

public class UserBlockPlaceDropHandler implements Consumer<UserBlockPlaceEvent> {

    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();

    @Override
    public void accept(UserBlockPlaceEvent userBlockPlaceEvent) {
        BlockPlaceEvent event = userBlockPlaceEvent.getBlockPlaceEvent();
        Player player = event.getPlayer();

    }
}
