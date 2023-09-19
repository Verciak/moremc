package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.Arrays;

public class HelpCommand 
{
    private final ToolsPlugin plugin;

    public HelpCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(value = { "pomoc" }, description = "Wyświetla komendy pomocy")
    public void handle(@Sender Player player) {
        Arrays.asList(
                "&f«&d&l&m----[&7&l&m---&f&m&l[--&f &d&lPOMOC &f&l&m--]&7&l&m---]&d&l&m----&8&f»",
                "&8» &d/os &8- &fOsiagniecia serwerowe",
                "&8» &d/gildia &8- &fInformacje na temat gildii",
                "&8» &d/tpa <nick> &8- &fProsba o teleportacje do danego gracza",
                "&8» &d/msg <nick> <wiadomosc> &8- &fPrywatna wiadomosc do danego gracza",
                "&8» &d/vip &8- &fInformacje dot rangi. &5VIP",
                "&8» &d/svip &8- &fInformacje dot rangi. &5SVIP",
                "&8» &d/helpop <wiadomosc> &8- &fWyslanie prosby o pomoc do admininstracji",
                "&8» &d/drop &8- &fDrop ze stone,case na serwerze",
                "&8» &d/sklep &8- &fSklep serwerowy",
                "&8» &d/otchlan &8- &fPrzedmioty z ziemii ktore mozesz zabrac",
                "&8» &d/schowek &8- &fSerwerowy schowek",
                "",
                "&8» &fStrona internetowa: &dhttps://www.paymc.pl",
                "&8» &fSklep serwerowy: &dhttps://www.paymc.pl/sklep",
                "&8» &fDiscord serwerowy: &dhttps://discord.gg/P9sEbgsFFR",
                "&f«&d&l&m----[&7&l&m---&f&m&l[--&f&l&m&8&f»     «&l&m--]&7&l&m---]&d&l&m----&8&f»"
        ).forEach(string -> player.sendMessage(MessageHelper.colored(string)));
    }
}
