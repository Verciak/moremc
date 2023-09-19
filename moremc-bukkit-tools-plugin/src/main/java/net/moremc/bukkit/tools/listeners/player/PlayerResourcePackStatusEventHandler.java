package net.moremc.bukkit.tools.listeners.player;

import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import net.moremc.bukkit.api.helper.MessageHelper;

public class PlayerResourcePackStatusEventHandler implements Listener {

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event){
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        Player player = event.getPlayer();
        if (status == PlayerResourcePackStatusEvent.Status.DECLINED || status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            Bukkit.getScheduler().runTaskLater(ToolsPlugin.getInstance(), () -> player.kickPlayer(MessageHelper.translateText("&cNie udało się pobrać txt zmień ustawienia")),60);
            return;
        }
        if (status == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            player.sendMessage(MessageHelper.translateText("&aPomyślnie pobrano texturepack serwera."));
        } else if (status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            player.sendMessage(MessageHelper.translateText("&aPomyślnie załadowano texturepack serwera."));
            Bukkit.getScheduler().runTaskLater(ToolsPlugin.getInstance(), () -> {
                player.playSound(player.getLocation(), "music.explode", 100F,6F);
            }, 10L);
        }
    }
}
