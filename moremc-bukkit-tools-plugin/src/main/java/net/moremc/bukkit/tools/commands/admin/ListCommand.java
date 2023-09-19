package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class ListCommand
{
    private final ToolsPlugin plugin;
    private final API api;

    private final UserService userService;
    private final SectorService sectorService;

    public ListCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();;
        this.sectorService = API.getInstance().getSectorService();
    }

    @Command(value = { "list", "online" }, description = "Wyświetla ilość graczy online na serwerze")
    public void handle(@Sender Player player) {

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.MODERATOR, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            player.sendMessage(MessageHelper.colored("&fAktualnie na serwerze przebywa &d" + sectorService.getOnline() + " &8/&55000 &fgraczy"));
        });
    }
}
