package net.moremc.guilds.listeners.player;

import net.moremc.guilds.event.UserSectorQuitEvent;
import net.moremc.guilds.event.handler.UserSectorQuitHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;

public class PlayerLeaveEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();
    private final UserSectorQuitHandler userSectorQuitHandler = new UserSectorQuitHandler();


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        userSectorQuitHandler.accept(new UserSectorQuitEvent
                (this.userService.findByValueOptional(player.getName()),
                        Bukkit.getPlayerExact(player.getName())));
    }
}