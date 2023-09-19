package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.player.PlayerGlobalMessagePacket;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

public class AdminChatCommand
{
    private final ToolsPlugin plugin;
    private final API api;

    private final UserService userService;

    public AdminChatCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "adminchat", "ac" }, description = "Wysyła wiadmość do administratorów")
    public void handle(@Sender Player player, @Name("message") @Combined String message) {

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if(!UserGroupType.hasPermission(UserGroupType.HELPER, user)){
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (message.trim().isEmpty()) {
                player.sendMessage("&cAby wysłać wiadmość należy podać treść!");
                return;
            }
            api.getNatsMessengerAPI().sendPacket("moremc_global_channel", new PlayerGlobalMessagePacket("&4[AdminChat] &c" + player.getName() + " &8&l» &f" + message, ""));
            player.sendMessage(MessageHelper.colored("&aPomyślnie wyslano wiadomość do admininstracji!"));
        });
    }
}
