package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.Arrays;

public class DiscordCommand
{
    private final ToolsPlugin plugin;
    private final UserService userService = API.getInstance().getUserService();
    public DiscordCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Command(value = { "discord", "dc" }, description = "Wyświetla informacje od discordzie")
    public void handle(@Sender Player player) {
        Arrays.asList(
                "",
                "&fDiscord: &ddc.paymc.pl",
                ""
        ).forEach(string -> player.sendMessage(MessageHelper.colored(string)));
    }
    @Command(value = { "weryfikacja", "verify" }, description = "Weryfikacja konta")
    public void handleVerify(@Sender Player player, @Name("message") @Combined String message) {
        this.userService.findUserByUUID(player.getUniqueId()).ifPresent(user -> {
            if (message.trim().isEmpty()) {
                throw new BladeExitMessage(MessageHelper.colored("&cAby się zweryfikować wpisz kod"));
            }
            if(user.isDiscord()){
                player.sendMessage("Twoje konto juz jest zweryfikowane");
                return;
            }
            if(user.getDiscordAuthorId() == null){
                player.sendMessage("Wejdz na discorda i wygeneruj sobie kod do weryfikacji");
                return;
            }
            if(user.getDiscordVerifyCode().equalsIgnoreCase(message)){
                user.setDiscord(true);
                player.sendMessage("Pomyslnie sie zweryfikowales");
            }
        });
    }
}