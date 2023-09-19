package net.moremc.guilds.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.guilds.GuildsPlugin;
import net.moremc.guilds.service.TeamService;

public class NameTagUpdateRunnable implements Runnable{

    private final TeamService teamService = GuildsPlugin.getInstance().getTeamService();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService= API.getInstance().getUserService();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                this.teamService.updateBoard(this.guildService.findGuildByNickName(player.getName()), user);
            });
        }
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(GuildsPlugin.getInstance(), this, 1L, 20L);
    }
}