package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.Arrays;
import java.util.Optional;

public class PlayerCommand
{
    private final ToolsPlugin plugin;

    private final UserService service;
    private final GuildService guildService;

    public PlayerCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.service = API.getInstance().getUserService();
        this.guildService = API.getInstance().getGuildService();
    }
    @Command(value = {"player", "gracz"}, description = "")
    public void handle(@Sender Player player, @Name("name") String name) {
        Optional<User> user = service.findByValueOptional(name);
        if (!user.isPresent()) {
            throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
        }
        Arrays.asList(
                "&f«&d&l&m----[&7&l&m---&f&m&l[--&f &d&lGRACZ &f&l&m--]&7&l&m---]&d&l&m----&8&f»",
                "&8» &fNick: &d" + user.get().getNickName(),
                "&8» &fRanga: " +  user.get().getGroupType().getPrefix(),
                "&8» &fGilida: " + (!guildService.findGuildByNickName(name).isPresent() ? "&cBrak" : " &8[&d" + guildService.findGuildByNickName(name).get().getName().toUpperCase() + "&8]"),
                "&8» &fPunkty: &a" + user.get().getUserStatics().getPoints(),
                "&8» &fZabojstwa: &a" + user.get().getUserStatics().getKills(),
                "&8» &fSmierci: &c" +  user.get().getUserStatics().getDeaths(),
                "&f«&d&l&m----[&7&l&m---&f&m&l[--&f&l&m&8&f»     «&l&m--]&7&l&m---]&d&l&m----&8&f»"
        ).forEach(string -> player.sendMessage(MessageHelper.colored(string)));
    }
}
