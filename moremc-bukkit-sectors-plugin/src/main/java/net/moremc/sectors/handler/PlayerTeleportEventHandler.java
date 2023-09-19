package net.moremc.sectors.handler;

import net.moremc.sectors.helper.SectorTransferHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;

public class PlayerTeleportEventHandler implements Listener {

    private final SectorService sectorService = API.getInstance().getSectorService();
    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Location locationTo = event.getTo();

        this.sectorService.findSectorByLocation(locationTo.getWorld().getName(), locationTo.getBlockX(), locationTo.getBlockZ()).ifPresent(sector -> {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                if (!user.getUserSector().isCanInteract()) return;
                SectorTransferHelper.changeSectorPlayer(player, sector, event.getTo(), event);
            });
        });
    }
}
