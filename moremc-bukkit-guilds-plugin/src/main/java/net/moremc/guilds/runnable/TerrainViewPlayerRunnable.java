package net.moremc.guilds.runnable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.sector.type.SectorType;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.guilds.GuildsPlugin;
import net.moremc.sectors.helper.SectorTransferHelper;

public class TerrainViewPlayerRunnable implements Runnable{


    private final UserService userService = API.getInstance().getUserService();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final SectorService sectorService = API.getInstance().getSectorService();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {
                this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                    if (!user.hasViewTerrainGuildTime()) {
                        player.teleport(new Location(player.getWorld(), user.getGuildAreaSelect().getX(), 100, user.getGuildAreaSelect().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        bukkitUser.updateActionBar(UserActionBarType.GUILD_TERRAIN, " &fPozostało&8: &d" + DataHelper.getTimeToString(user.getViewTerrainGuildTime()));
                    } else {
                        if (user.getLocationBeforeCreate() != null) {
                            player.setAllowFlight(UserGroupType.hasPermission(UserGroupType.HELPER, user));
                            player.setFlySpeed(0.1F);
                            bukkitUser.removeActionBar(UserActionBarType.GUILD_TERRAIN);

                            this.sectorService.findSectorByLocation("world", user.getLocationBeforeCreate().getX(), user.getLocationBeforeCreate().getZ()).ifPresent(sector -> {
                                Location location = new Location(Bukkit.getWorld("world"), user.getLocationBeforeCreate().getX(), user.getLocationBeforeCreate().getY(), user.getLocationBeforeCreate().getZ());
                                user.setLocationBeforeCreate(null);

                                if(!sector.getInfo().isOnline()){
                                    this.sectorService.findRandomSectorOnlineByType(SectorType.SPAWN).ifPresent(sectorFind -> {
                                        SectorTransferHelper.saveDataPlayerSector(player, location, user, true, sectorFind.getName());
                                    });
                                    return;
                                }
                                SectorTransferHelper.saveDataPlayerSector(player, location, user, true, sector.getName());
                            });
                        }
                    }
                });
            });
        }
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimer(GuildsPlugin.getInstance(), this, 1L, 20L);
    }
}
