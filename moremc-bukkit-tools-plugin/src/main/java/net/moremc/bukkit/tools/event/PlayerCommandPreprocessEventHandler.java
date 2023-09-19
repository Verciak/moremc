package net.moremc.bukkit.tools.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Objects;

public class PlayerCommandPreprocessEventHandler implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event){
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0];
        HelpTopic helpTopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
        if (Objects.isNull(helpTopic)) {
            event.setCancelled(true);
            player.sendTitle("", MessageHelper.colored("&fWykryto nieznaną komende wpisz&8: &d/pomoc"));
        }
    }
}
