package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.TeleportHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class SpawnCommand
{
    private ToolsPlugin plugin;
    private API api;

    private UserService userService;

    public SpawnCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();;
    }
    @Command(value = { "spawn" }, description = "Telportuje na spawn")
    public void handle(@Sender Player player) {
        userService.findByValueOptional(player.getName()).ifPresent(user -> {
            TeleportHelper.teleport(player, (UserGroupType.hasPermission(UserGroupType.HELPER, user)) ? 0 : System.currentTimeMillis() + TimeType.SECOND.getTime(10),
                    new Location(Bukkit.getWorld("world"), 0, 100, 0));
        });
    }
}