package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

public class MuteCommand
{
    private final UserService userService = API.getInstance().getUserService();
    private final ToolsPlugin plugin;

    public MuteCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(value = {"mute"}, description = "")
    public void handle(@Sender Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            //TODO
        });
    }
    @Command(value = {"unmute"}, description = "")
    public void handleUnMute(@Sender Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            //TODO
        });
    }
}
