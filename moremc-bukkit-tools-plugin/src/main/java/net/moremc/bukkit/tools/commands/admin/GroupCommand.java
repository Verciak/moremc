package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCommand {
    private final ToolsPlugin toolsPlugin;
    private final API api;

    private final UserService userService;

    public GroupCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
    }


    @Command(value = {"group"}, description = "Komendy do ustawiania rangi")
    public void handle(@Sender final CommandSender sender, @Name("group") final String group, @Name("nick") final String nick) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
                if (!UserGroupType.hasPermission(UserGroupType.ROOT, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }

                if (UserGroupType.groupExists(group.toUpperCase())) {
                    throw new BladeExitMessage(MessageHelper.colored("&cPodana grupa nie istnieje!"));
                }

                if(!this.userService.findByValueOptional(nick).isPresent()){
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cPodany gracz nigdy nie był na serwerze."));
                }
                this.userService.findByValueOptional(nick).ifPresent(userFind -> {
                    userFind.setGroupType(UserGroupType.valueOf(group.toUpperCase()));
                    player.sendMessage(MessageHelper.translateText("&aPomyślnie nadano range&8: " + UserGroupType.valueOf(group.toUpperCase()).getPrefix() + " &fgraczowi&8: " + userFind.getNickName()));
                    this.userService.synchronizeUser(SynchronizeType.UPDATE, userFind);
                });
            });
        }else{
            if (UserGroupType.groupExists(group.toUpperCase())) {
                throw new BladeExitMessage(MessageHelper.colored("&cPodana grupa nie istnieje!"));
            }

            if(!this.userService.findByValueOptional(nick).isPresent()){
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cPodany gracz nigdy nie był na serwerze."));
            }
            this.userService.findByValueOptional(nick).ifPresent(userFind -> {
                userFind.setGroupType(UserGroupType.valueOf(group.toUpperCase()));
                sender.sendMessage(MessageHelper.translateText("&aPomyślnie nadano range&8: " + UserGroupType.valueOf(group.toUpperCase()).getPrefix() + " &fgraczowi&8: " + userFind.getNickName()));
                this.userService.synchronizeUser(SynchronizeType.UPDATE, userFind);
            });
        }
    }
}
