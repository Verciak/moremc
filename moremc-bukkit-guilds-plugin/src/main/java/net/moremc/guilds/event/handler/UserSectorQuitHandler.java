package net.moremc.guilds.event.handler;


import net.moremc.guilds.GuildsPlugin;
import net.moremc.guilds.service.TeamService;
import org.bukkit.entity.Player;
import net.moremc.guilds.event.UserSectorQuitEvent;
import net.moremc.sectors.SectorPlugin;
import net.moremc.sectors.helper.SectorTransferHelper;

import java.util.function.Consumer;

public class UserSectorQuitHandler implements Consumer<UserSectorQuitEvent> {


    private final TeamService teamService = GuildsPlugin.getInstance().getTeamService();


    @Override
    public void accept(UserSectorQuitEvent userSectorQuitEvent) {
        Player player = userSectorQuitEvent.getPlayer();

        userSectorQuitEvent.getOptionalUser().ifPresent(user -> {
            this.teamService.removeNameTag(player, user);

            if(user.isViewTerrainGuild() || !user.hasViewTerrainGuildTime()){
                user.setGuildCreatedLeave(true);
                return;
            }
            if(user.getUserSector().getSectorChange() <= System.currentTimeMillis()){
                SectorTransferHelper.saveDataPlayerSector(player, player.getLocation(), user,false, SectorPlugin.getInstance().getSectorName());
            }
        });
    }
}
