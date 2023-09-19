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
import net.moremc.bukkit.tools.inventories.admin.AdminPanelInventory;

public class AdminPanelCommand
{
    private final ToolsPlugin plugin;

    private final UserService userService;

    public AdminPanelCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.userService = API.getInstance().getUserService();
    }

    @Command(value = { "adminpanel", "ap" }, description = "Otwiera GUI do zarzadzania całym serwerem")
    public void handle(@Sender Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HEADADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            new AdminPanelInventory().show(player);
        });
    }
}
