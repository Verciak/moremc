package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.*;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class SpeedCommand
{
    private final ToolsPlugin plugin;

    private final UserService userService;

    public SpeedCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "speed", "flyspeed", "walkspeed" }, description = "Zmiania predkości latania lub chodzenia")
    public void handle(@Sender Player player, final @Name("speed") @Range(min = 1D, max = 10D) double speed) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            float finalSpeed = (float) (speed / 10F);
            if(finalSpeed > 10.0f) {
                player.sendMessage(MessageHelper.colored("&cPodana wartość nie może być wieksza niż: 10.0"));
                return;
            }
            if (player.isFlying()) {
                player.setFlySpeed(finalSpeed);
                player.sendMessage(MessageHelper.colored("&fSzybkość &dlatania &fzostała ustawiona na &d" + finalSpeed));
                return;
            }
            player.setWalkSpeed(finalSpeed);
            player.sendMessage(MessageHelper.colored("&fSzybkość &dchodzenia &fzostała ustawiona na &d" + finalSpeed));
        });
    }
}
