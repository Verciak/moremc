package net.moremc.sectors.handler;

import net.moremc.sectors.SectorPlugin;
import net.moremc.sectors.helper.SectorTransferHelper;
import net.moremc.sectors.holder.BorderHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;

public class PlayerMoveEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();
    private final SectorService sectorService = API.getInstance().getSectorService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location locationFrom = event.getFrom();
        Location locationTo = event.getTo();
        if (locationFrom.getBlockX() == locationTo.getBlockX() && locationFrom.getBlockY() == locationTo.getBlockY() && locationFrom.getBlockZ() == locationTo.getBlockZ())
            return;

        BorderHolder.update(player);

        Bukkit.getScheduler().runTaskAsynchronously(SectorPlugin.getInstance(), () -> {
            this.sectorService.findSectorByLocation(locationTo.getWorld().getName(), locationTo.getBlockX(), locationTo.getBlockZ()).ifPresent(sector -> {
                this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                    SectorTransferHelper.changeSectorPlayer(player, sector, locationTo, event);
                });
            });
        });
    }
}
