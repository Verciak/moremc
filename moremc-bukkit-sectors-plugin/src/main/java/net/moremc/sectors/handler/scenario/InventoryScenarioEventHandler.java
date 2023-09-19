package net.moremc.sectors.handler.scenario;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;

public class InventoryScenarioEventHandler implements Listener {


    private final UserService userService = API.getInstance().getUserService();


    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        if(event.isCancelled())return;

        Player player = (Player) event.getWhoClicked();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()){
                event.setCancelled(true);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDragClick(InventoryDragEvent event){
        if(event.isCancelled())return;

        Player player = (Player) event.getWhoClicked();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()){
                event.setCancelled(true);
            }
        });
    }


}
