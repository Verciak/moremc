package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.Arrays;

public class VIPCommand
{
    private final ToolsPlugin plugin;

    public VIPCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(value = { "vip" }, description = "Wyświetla informacje o randze vip")
    public void handle(@Sender Player player) {
        Arrays.asList(
                " ",
                "'                        &5&lVIP",
                "",
                "                &7• &aUPRAWNIENIA:",
                "",
                " &8&l>> &f/workbench &7- Darmowy crafting.",
                " &8&l>> &f/ec &7- &5&l3 &7darmowe enderchesty.",
                " &8&l>> &f/repair &7- Darmowa naprawa itemow.",
                "",
                "                &7• &aPRZYWILEJE:",
                " &8&l>> &7Dostep do &5&lwszystkich &7home.",
                " &8&l>> &7Dostepne zestawy: &7gracz&7, &5&lVIP",
                " &7oraz pare innych fajnych kitow.",
                " &8&l>> &cOraz wiele wiecej!",
                " &8&l>> &cKup i przekonaj sie sam!",
                "",
                "               &7• &dZAKUPISZ NA:",
                "",
                " &8&l>> &7Strona: &fhttps://www.paymc.pl/sklep",
                ""
        ).forEach(string -> player.sendMessage(MessageHelper.colored(string)));
    }
}
