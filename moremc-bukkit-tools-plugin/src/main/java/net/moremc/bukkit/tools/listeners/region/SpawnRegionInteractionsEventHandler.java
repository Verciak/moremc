package net.moremc.bukkit.tools.listeners.region;

import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SpawnRegionInteractionsEventHandler implements Listener {

    private final SectorService sectorService = API.getInstance().getSectorService();
    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(this.sectorService.getCurrentSector().get().isSpawn() && !UserGroupType.hasPermission(UserGroupType.ROOT, user)){
                event.setCancelled(true);
                player.sendMessage(MessageHelper.translateText("&cZnajdujesz się na spawnie."));
            }
        });
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                if (this.sectorService.getCurrentSector().get().isSpawn() && !UserGroupType.hasPermission(UserGroupType.ROOT, user)) {
                    event.setCancelled(true);
                }
            });
        }
    }
}
