package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BroadcastCommand
{
    private final ToolsPlugin toolsPlugin;
    private final API api;

    private final UserService userService;

    public BroadcastCommand(final ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
    }

    @Command(value = { "broadcast" , "bc"}, description = "Komendy broadcast")
    public void handle(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            Arrays.asList(
                    "",
                    " &8» &d/broadcast chat &8- &fWysyła wszystkim wiadmosć na chacie",
                    " &8» &d/broadcast title &8- &fWysyła wszystkim wiadmosć na title",
                    " &8» &d/broadcast actionbar &8- &fWysyła wszystkim wiadmosć na actionbarze",
                    ""
            ).forEach(s -> player.sendMessage(MessageHelper.colored(s)));
        });
    }
    @Command(value = { "broadcast chat", "bc chat" }, description = "Wysyła wszystkim wiadmosć na chacie")
    public void handleChat(@Sender final Player player, @Name("message") final String message) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (message.trim().isEmpty()) {
                throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket(message, SendMessageReceiverType.ALL, SendMessageType.CHAT));
        });
    }

    @Command(value = { "broadcast title", "bc title" }, description = "Wysyła wszystkim wiadmosć na title")
    public void handleTitle(@Sender final Player player, @Name("message") final String message) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (message.trim().isEmpty()) {
                throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket(message, SendMessageReceiverType.ALL, SendMessageType.TITLE));
        });
    }

    @Command(value = { "broadcast actionbar", "bc actionbar" }, description = "Wysyła wszystkim wiadmosć na actionbarze")
    public void handleActionBar(@Sender final Player player, @Name("message") final String message) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (message.trim().isEmpty()) {
                throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket(message, SendMessageReceiverType.ALL, SendMessageType.ACTIONBAR));
        });
    }
}
