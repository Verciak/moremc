package net.moremc.sectors.handler.scenario;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;

public class EntityDamageScenarioEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event){
        if(event.isCancelled())return;

        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                if(!user.getUserSector().isCanInteract()){
                    event.setCancelled(true);
                }
            });
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.isCancelled())return;
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player playerEntity = (Player) event.getEntity();
            Player playerDamager = (Player) event.getDamager();
            this.userService.findByValueOptional(playerEntity.getName()).ifPresent(user -> {

                if(!user.getUserSector().isCanInteract()){
                    event.setCancelled(true);
                }
            });
            this.userService.findByValueOptional(playerDamager.getName()).ifPresent(user -> {
                if(!user.getUserSector().isCanInteract()) {
                    event.setCancelled(true);
                }
            });

        }

    }
}
