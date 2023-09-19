package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CaseCommand
{
    private final ToolsPlugin toolsPlugin;
    private final API api;

    private final UserService userService;

    public CaseCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "case"}, description = "Komenda do nadawania dla gracza")
    public void handle(@Sender Player player, @Name("name") @Combined String name, @Name("size") @Combined int size) {
        userService.findByValueOptional(player.getName()).ifPresent(userg -> {
            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, userg)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            Optional<User> user = api.getUserService().findByValueOptional(name);
            if (!user.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
            }
            //TODO ADD CASE TO DATABASE
        });
    }
    @Command(value = { "case all"}, description = "Komenda do nadawania skrzynki dla całego serwer")
    public void handle(@Sender Player player, @Name("size") @Combined int size) {
        userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            //TODO ADD CASE TO DATABASE
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new ClientSendMessagePacket("&fAdministrator &d" + player.getName() + " &frozdał calemu serwerowi &d&lMAGICZNA SKRZYNIA &fx&d" + size, SendMessageReceiverType.ALL, SendMessageType.CHAT));
        });
    }
}
