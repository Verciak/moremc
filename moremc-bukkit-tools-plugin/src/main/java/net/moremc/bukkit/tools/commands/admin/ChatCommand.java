package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.server.Server;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.ServerService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class ChatCommand
{
    private final ToolsPlugin toolsPlugin;
    private final API api;

    private final UserService userService;
    private final ServerService serverService;

    private static final char[] CHARS = new char[7680];

    static {
        Arrays.fill(CHARS, ' ');
    }

    public ChatCommand(final ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.api = API.getInstance();

        this.userService = API.getInstance().getUserService();
        this.serverService = API.getInstance().getServerService();
    }

    @Command(value = { "chat" }, description = "Komendy chatu")
    public void handle(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.MODERATOR, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            Arrays.asList(
                    "",
                    " &8» &d/chat clear &8- &fCzyści chat",
                    " &8» &d/chat on &8- &fWłącza czat na calym serwerze",
                    " &8» &d/chat off &8- &fWyłącza czat na calym serwerze",
                    ""
            ).forEach(s -> player.sendMessage(MessageHelper.colored(s)));
        });
    }

    @Command(value = { "chat clear" , "chat cc" }, description = "Czyści chat")
    public void handleClear(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.MODERATOR, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket(new String(CHARS) + "\n" + "&fChat został &bwyczysczony &fprzez &d" + player.getName() + "&f!", SendMessageReceiverType.ALL, SendMessageType.CHAT));
        });
    }
    @Command(value = { "chat on" }, description = "Włącza czat na calym serwerze")
    public void handleEnable(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.MODERATOR, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            Optional<Server> server = serverService.findByValueOptional(1);
            if(!server.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("Error"));
            }
            server.get().setChatStatus(true);
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket(new String(CHARS) + "\n" + "&fChat został &awłączony &fprzez &d" + player.getName() + "&f!", SendMessageReceiverType.ALL, SendMessageType.CHAT));
        });
    }
    @Command(value = { "chat off" }, description = "Wyłącza czat na calym serwerze")
    public void handleDisable(@Sender final Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.MODERATOR, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            Optional<Server> server = serverService.findByValueOptional(1);
            if(!server.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("Error"));
            }
            if(!server.get().isChatStatus()) {
                throw new BladeExitMessage(MessageHelper.colored("&cChat jest już wyłączony!"));
            }
            server.get().setChatStatus(false);
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket(new String(CHARS) + "\n" + "&fChat został &cwyłaczony &fprzez &d" + player.getName() + "&f!", SendMessageReceiverType.ALL, SendMessageType.CHAT));
        });
    }
}
