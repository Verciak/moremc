package net.moremc.sectors.event.handler;

import net.moremc.sectors.SectorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.moremc.api.sector.Sector;
import net.moremc.sectors.event.UserSectorChangeEvent;
import net.moremc.sectors.helper.SectorTransferHelper;

import java.util.function.Consumer;

public class UserSectorChangeHandler implements Consumer<UserSectorChangeEvent> {


    @Override
    public void accept(UserSectorChangeEvent userSectorChangeEvent) {
        Player player = userSectorChangeEvent.getPlayer();
        Sector sector = userSectorChangeEvent.getSector();
        Location location = userSectorChangeEvent.getLocationTo();

        userSectorChangeEvent.getOptionalUser().ifPresent(user -> {

            if(!user.getUserSector().hasSectorChangeDelay()){
                return;
            }
            user.getUserSector().setCanInteract(false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(SectorPlugin.getInstance(), () -> SectorTransferHelper.saveDataPlayerSector(player, location, user,true, sector.getName()));
        });
    }
}
