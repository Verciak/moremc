package net.moremc.sectors.handler.scenario;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;

public class PlayerInteractScenarioEventHandler implements Listener {


    private final UserService userService= API.getInstance().getUserService();


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.isCancelled())return;
        Player player=  event.getPlayer();

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()) {
                event.setCancelled(true);
            }
        });
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()) {
                event.setCancelled(true);
            }
        });
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()) {
                event.setCancelled(true);
            }
        });
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()) {
                event.setCancelled(true);
            }
        });
    }

}
