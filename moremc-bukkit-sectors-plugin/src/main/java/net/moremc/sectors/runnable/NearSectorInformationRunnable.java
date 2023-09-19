package net.moremc.sectors.runnable;

import net.moremc.api.API;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.sectors.SectorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class NearSectorInformationRunnable implements Runnable {

    private final SectorService sectorService = API.getInstance().getSectorService();
    private final DecimalFormat decimalFormat = new DecimalFormat("##.##");
    private final BukkitCache userCache = BukkitAPI.getInstance().getBukkitCache();

    public void start() {
        Bukkit.getScheduler().runTaskTimer(SectorPlugin.getInstance(), this, 0L, 0L);
    }
    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!API.getInstance().getSectorService().getCurrentSector().isPresent()) return;
            Sector sector = API.getInstance().getSectorService().getCurrentSector().get();
            Location location = player.getLocation();
            this.userCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {
                double distance = sector.getInfo().getLocationInfo().getDistanceToBorder(location.getBlockX(), location.getBlockZ());
                if (distance > 30) {
                    bukkitUser.removeActionBar(UserActionBarType.BORDER);
                    return;
                }
                Sector nearestSector = sector.getInfo().getLocationInfo().getNearestSector(distance, location.getBlockX(), location.getBlockZ(), location.getWorld().getName(), this.sectorService);
                if (nearestSector == null) {
                    bukkitUser.updateActionBar(UserActionBarType.BORDER, "&5Granica mapy: &8(&d" + this.decimalFormat.format(distance) + "m&8)");

                    return;
                }

                if (nearestSector.isSpawn() && API.getInstance().getSectorService().getCurrentSector().get().isSpawn()) return;
                bukkitUser.updateActionBar(UserActionBarType.BORDER,
                        "&fZbliżasz się do granicy sektora: &8(&5" + this.decimalFormat.format(distance) + "m&7, &d" + nearestSector.getName().toUpperCase() + "&7, " + (nearestSector.getInfo().isOnline() ? "§aOnline" : "§cOffline") + "&8)");
            });
        }
    }
}