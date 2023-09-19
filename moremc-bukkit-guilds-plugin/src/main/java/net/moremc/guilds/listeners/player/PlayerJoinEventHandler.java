package net.moremc.guilds.listeners.player;

import net.moremc.guilds.event.UserSectorJoinEvent;
import net.moremc.guilds.event.handler.UserSectorJoinHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;

public class PlayerJoinEventHandler implements Listener {


    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        UserSectorJoinHandler sectorJoinHandler  = new UserSectorJoinHandler();
        sectorJoinHandler.accept(new UserSectorJoinEvent(API.getInstance().getSectorService().getCurrentSector(),
                this.userService.findByValueOptional(player.getName()), player));
    }

}
