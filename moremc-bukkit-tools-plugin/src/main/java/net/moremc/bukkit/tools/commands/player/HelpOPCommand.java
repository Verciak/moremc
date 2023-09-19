package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.client.player.PlayerGlobalMessagePacket;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

public class HelpOPCommand
{
    private final ToolsPlugin plugin;

    private final UserService userService;

    public HelpOPCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.userService = API.getInstance().getUserService();
    }

    @Command(value = { "helpop" }, description = "Wysyła wiadmość do wszystkich administratorów")
    public void handle(@Sender Player player, @Name("message") @Combined String message) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(!user.hasCooldown("helpop")) {
                throw new BladeExitMessage(MessageHelper.colored("&cNastępną wiadmość możesz wysłać za &7" + DataHelper.getTimeToString(user.getCooldownTime("helpop")) + " &c!"));
            }
            if (message.trim().isEmpty()) {
                throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
            }
            user.addCooldown("helpop", System.currentTimeMillis() + TimeType.SECOND.getTime(60));

            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_global_channel", new PlayerGlobalMessagePacket("&c[HelpOP] &8(&d" + API.getInstance().getSectorService().getCurrentSector().get().getName() +"&8) &7" + player.getName() + " &8&l» &f" + message, "moremc.helpop.view"));
            player.sendMessage(MessageHelper.translateText("&aPomyślnie wyslano wiadomość do admininstracji!"));
        });
    }
}
