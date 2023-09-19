package net.moremc.bukkit.tools.commands.admin;


import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.player.PlayerGlobalMessagePacket;
import net.moremc.api.nats.packet.client.player.PlayerKickPacket;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

import java.util.Optional;

public class KickCommand
{

    private final ToolsPlugin plugin;
    private final API api;

    private final UserService userService;

    public KickCommand(final ToolsPlugin plugin) {
        this.plugin = plugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
    }

    @Command(value = { "kick", "wyrzuć" }, description = "Wyrzuca gracza z sektora")
    public void handle(@Sender final Player player, @Name("name") final String name, @Name("reason") final String reason) {
        this.userService.findByValueOptional(player.getName()).ifPresent(userSender -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, userSender)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            Optional<User> user = api.getUserService().findByValueOptional(name);
            if (!user.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
            }
            if (!user.get().getNickName().equalsIgnoreCase(name)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie możesz wyrzucić samego siebie"));
            }
            if (!api.getSectorService().isOnlinePlayer(name)) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cjest aktualnie offline!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_global", new PlayerGlobalMessagePacket("&4Gracz &c" + name + " &4został wyrzucony przez &c" + player.getName() + " &4Pow\u00f3d: &c" + reason, ""));
            api.getNatsMessengerAPI().sendPacket("moremc_proxies_proxy01", new PlayerKickPacket(name, getMessage(player.getName(), reason))); //TODO USER PROXY
        });
    }

    private String getMessage(String admin, String reason) {
        return "&8&m----[&4&m----[&8&m---[ &8 &c&l!!! &8&m ]---&4&m]----&8&m]----&8" +
               "                 &cZostałeś wyrzucony z serwera \n" +
               "\n                     " +
               "&fAdmininstrator: &d" + admin + "\n " +
               "&fPowód: &d" + reason + "\n" +
                "  \n " +
                "   &7Zazwyczaj nie dzieje się to bez powodu\n  " +
                "      &a&n&lWEJDŻ PONOWNIE DO GRY";
    }
}
