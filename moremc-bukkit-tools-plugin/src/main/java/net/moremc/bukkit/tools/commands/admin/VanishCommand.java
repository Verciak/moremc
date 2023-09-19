package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class VanishCommand
{
    private final ToolsPlugin toolsPlugin;

    private final UserService userService;

    public VanishCommand(final ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.userService = API.getInstance().getUserService();
    }

    @Command(value = { "vanish", "v" }, description = "Włącza ukryty tryb")
    public void handle(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if(!user.isVanish()){
                user.setVanish(true);
                player.sendTitle(MessageHelper.translateText("&d&lVANISH"), MessageHelper.translateText("&aWłączono &fpomyślnie &dniewidzialność"));
                return;
            }
            user.setVanish(false);
            player.sendTitle(MessageHelper.translateText("&d&lVANISH"), MessageHelper.translateText("&cWyłączono &fpomyślnie &dniewidzialność"));

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(player);
            }
        });
    }
}
