package net.moremc.bukkit.api.helper;

import net.moremc.api.helper.DataHelper;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.bukkit.api.BukkitAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class TeleportHelper {


    public static Set<String> teleportList = new HashSet<>();


    public static void teleport(Player player, long time, Location location){
        if(teleportList.contains(player.getName())){
            new MessageHelper(player, MessageHelper.colored("&4Błąd: &cJuż się teleportujesz."));
            return;
        }
        teleportList.add(player.getName());

        new BukkitRunnable(){

            long timeTeleport = time;
            Location playerLocation = player.getLocation();
            MessageHelper messageHelper = new MessageHelper(player, "");

            @Override
            public void run() {

                messageHelper.setMessage(MessageHelper.colored(
                        "&fTeleportacja &d" + DataHelper.getTimeToString(timeTeleport)));
                messageHelper.send(SendMessageType.TITLE);

                if(timeTeleport <= System.currentTimeMillis()){
                    player.teleport(location);
                    this.cancel();
                    teleportList.remove(player.getName());
                    return;
                }
                if(!playerHasMove(playerLocation, player.getLocation())){
                    messageHelper.setMessage(MessageHelper.colored( "&cPodczas teleportacji nie wolno się ruszać."));
                    messageHelper.send(SendMessageType.TITLE);
                    this.cancel();
                    teleportList.remove(player.getName());
                }
            }
        }.runTaskTimer(BukkitAPI.getInstance(), 1L, 20L);
    }
    public static boolean playerHasMove(Location locationPlayer, Location locationActual){
        return locationPlayer.getBlockX() == locationActual.getBlockX() && locationPlayer.getBlockY() == locationActual.getBlockY() && locationPlayer.getBlockZ() == locationActual.getBlockZ();
    }

}
