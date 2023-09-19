package net.moremc.guilds.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import net.moremc.api.API;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.PlayerCameraHelper;

public class PlayerToggleShiftEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onShiftToggle(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if(!event.isSneaking()) {
                if (user.isPeriscope()) {
                    user.setPeriscope(false);
                    this.userService.synchronizeUser(SynchronizeType.UPDATE, user);
                    PlayerCameraHelper.removeCamera(player);
                }
            }
        });
    }
}
