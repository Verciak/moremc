package net.moremc.bukkit.tools.listeners.player;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;
import net.moremc.api.API;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Arrays;
import java.util.Objects;

public class PlayerCommandPreprocessEventHandler implements Listener {


    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0];
        HelpTopic helpTopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);


        String message = event.getMessage();
        String[] splittedMessage = message.split(" ");
        String[] pluginCommands = {"/pl", "/plugins", "/?", "/help", "/ver", "/version", "/bukkit", "/bukkit:ver", "/bukkit:version", "/icanhasbukkit", "/bukkit:help", "bukkit:?", "/me", "/bukkit:me", "/minecraft:me", "/about"};
        String[] commandsLogoutCheck = {"/spawn", "/tpa", "/tpaccept", "/kit", "/sethome", "/home",  "/is", "/msg", "/g", "/gildia", "/gildie", "/guild",
        "/bazar", "/odbierz", "/schowek", "/ec", "/wb", "/crafting", "/craftingi", "/receptury", "/repair", "/repairall", "/naprawwszystko", "/napraw", "/cx", "/cobblex",
        "/incognito", "/os", "/osiagniecia"};

        String[] commandsGuildCheck = {"/tpaccept"};

        if (Objects.isNull(helpTopic) || StringUtils.containsIgnoreCase(Arrays.toString(pluginCommands), splittedMessage[0])) {
            event.setCancelled(true);
            player.sendTitle("", MessageHelper.colored("&fWykryto nieznaną komende wpisz&8: &d/pomoc"));
            return;
        }
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if (!user.getUserAntiLogout().hasAntiLogout()){
                for (String commandName : commandsLogoutCheck) {
                    if (event.getMessage().toLowerCase().contains(commandName)) {
                        event.setCancelled(true);
                        player.sendTitle("", MessageHelper.colored("&cPodczas walki nie wolno użyć tej komendy."));
                        return;
                    }
                }
            }
            this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
                if(guild.getLocation() != null && guild.isOnCuboid(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ())){
                    for (String commandName : commandsGuildCheck) {
                        if (event.getMessage().toLowerCase().contains(commandName)) {
                            event.setCancelled(true);
                            player.sendTitle("", MessageHelper.colored("&cNa terenie gildii nie możesz użyć tej komendy."));
                        }
                    }
                }

            });

        });
    }
}

