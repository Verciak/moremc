package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class GodCommand
{
    private final ToolsPlugin toolsPlugin;

    private final UserService userService;

    public GodCommand(final ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.userService = API.getInstance().getUserService();
    }

    @Command(value = { "god" , "niesmiertelnosc"}, description = "Zmienia twój tryb goda")
    public void handle(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            user.setGod(!user.isGod());
            player.sendMessage(MessageHelper.colored("&fTryb &dniesmiertelnosci &fzostał " + (user.isGod() ? "&aWlaczony" : "&cWylaczony")));
        });
    }
}
