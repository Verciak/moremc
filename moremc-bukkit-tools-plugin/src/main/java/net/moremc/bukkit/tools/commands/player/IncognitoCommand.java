package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import net.moremc.bukkit.tools.utilities.SkinUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class IncognitoCommand {

    private final ToolsPlugin plugin;
    private final UserService userService;

    public IncognitoCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "incognito"}, description = "")
    public void handle(@Sender Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            user.setIncognito(!user.isIncognito());
            player.sendTitle("", MessageHelper.translateText("&fPomyślnie " + (user.isIncognito() ? "&awłączono" : "&cwyłączono") + " &fincognito&8(&a◉&8)"));


            if(!user.isIncognito()){
                new SkinUtilities().removeSkin(player);
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.hidePlayer(player);
                    onlinePlayer.showPlayer(player);
                }
                user.setFakeNickname(user.getNickName());
                return;
            }
            new SkinUtilities().changeSkin(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(player);
                onlinePlayer.showPlayer(player);
            }
        });
    }
}