package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeCommand {

    private final UserService userService;

    public GamemodeCommand() {
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "gamemode", "gm", }, description = "Zmiania trybu gry")
    public void handle(@Sender Player player, final @Name("gamemode") GameMode mode) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HEADADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }

            if(mode == null) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie znaleziono podanego tryb gamemode"));
            }
            player.setGameMode(mode);
            player.sendMessage(MessageHelper.colored("&fTwoj tryb gry zostal zmieniony na: &d" + mode.name()));
        });
    }
}
