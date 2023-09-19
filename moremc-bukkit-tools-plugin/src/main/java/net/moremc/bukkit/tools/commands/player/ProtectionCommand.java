package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class ProtectionCommand
{
    private final ToolsPlugin plugin;

    private final UserService userService;

    public ProtectionCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "protection", "ochrona" }, description = "Wyświetla informacje o ochronie startowej")
    public void handle(@Sender Player player) {
        userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.hasProtection()){
                player.sendMessage(MessageHelper.colored("&cNie posiadasz już ochrony startowej."));
                return;
            }
            player.sendMessage(MessageHelper.colored("   &fTwoja ochrona będzie jeszcze aktywna przez &d" + DataHelper.getTimeToString(user.getCooldownTime("protection"))));
            player.sendMessage(MessageHelper.colored("  &fAby ja wyłączyć wpisz komendę &d/ochrona off"));
        });
    }
    @Command(value = { "protection off", "ochrona off" }, description = "Wylacza ochrone startowa")
    public void handleDisable(@Sender Player player) {
        userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.hasProtection()){
                player.sendMessage(MessageHelper.colored("&cNie posiadasz już ochrony startowej."));
                return;
            }
            user.addCooldown("protection", 0L);
            player.sendTitle(MessageHelper.translateText("&d&lOCHRRONA"), MessageHelper.translateText("&fTwoja ochrona została pomyślnie &cwyłączona"));
        });
    }
}