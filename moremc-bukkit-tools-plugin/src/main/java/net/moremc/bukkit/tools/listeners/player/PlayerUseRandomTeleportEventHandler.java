package net.moremc.bukkit.tools.listeners.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Button;
import org.bukkit.potion.PotionEffect;
import net.moremc.api.API;
import net.moremc.api.sector.Sector;
import net.moremc.api.sector.type.SectorType;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.RandomTeleportHelper;
import net.moremc.bukkit.api.utilities.RandomUtilities;

public class PlayerUseRandomTeleportEventHandler implements Listener {


    private final SectorService sectorService = API.getInstance().getSectorService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.isCancelled())return;


        if (!event.hasBlock()) {
            return;
        }

        Player player = event.getPlayer();
        Block clicked = event.getClickedBlock();


        if (clicked.getType() == Material.STONE_BUTTON) {
            Button btn = (Button) clicked.getState().getData();
            Block base = clicked.getRelative(btn.getAttachedFace());
            if (base.getType() != Material.JUKEBOX) {
                return;
            }
            if (this.sectorService.getCurrentSector().get().isSpawn()) {
                randomTp(event.getPlayer());
            }
        }
        clicked = event.getClickedBlock();
        if (clicked.getType() == Material.WOOD_BUTTON) {
            Button btn = (Button) clicked.getState().getData();
            Block base = clicked.getRelative(btn.getAttachedFace());
            if (base.getType() != Material.JUKEBOX) {
                return;
            }

            if (event.getClickedBlock().getState().getLocation().distance(player.getLocation()) > 2) {
                player.sendMessage(MessageHelper.colored("&4Błąd: &cPrzybliż się do guzika"));
                return;
            }

            Sector sector = this.sectorService.findSectorOnlineByType(SectorType.GUILD);
            if (sector == null) {
                event.setCancelled(true);
                player.sendMessage(MessageHelper.colored("&4Błąd: &cNie można odnależć sektora teparkowego!!"));
                return;
            }
            new RandomTeleportHelper(player, player.getWorld(), -1500, 1500, -1500, 1500).apply(randomTeleportHelper -> {
                int x = RandomUtilities.getRandInt(Math.min(sector.getInfo().getLocationInfo().getMinX(), sector.getInfo().getLocationInfo().getMaxX()), Math.max(sector.getInfo().getLocationInfo().getMinX(), sector.getInfo().getLocationInfo().getMaxX()));
                int z = RandomUtilities.getRandInt(Math.min(sector.getInfo().getLocationInfo().getMinZ(), sector.getInfo().getLocationInfo().getMaxZ()), Math.max(sector.getInfo().getLocationInfo().getMinZ(), sector.getInfo().getLocationInfo().getMaxZ()));
                double y = player.getWorld().getHighestBlockYAt(x, z) + 1.5f;
                randomTeleportHelper.setFoundedLocation(new Location(player.getWorld(), x, y, z));
                randomTeleportHelper.setCurrent(event.getClickedBlock().getState().getLocation());

                randomTeleportHelper.findPlayers(5, 2);

                if (randomTeleportHelper.getPlayerList().size() < 2) {
                    player.sendMessage(MessageHelper.colored("&5&lGrupowyTeleport &8>> &fPoczekaj na swojego przeciwnika!"));
                    return;
                }
                randomTeleportHelper.sendMessagePlayers("&5&lGrupowyTeleport &8>> &fNa pojedynek ida: ");
                for (Player players : randomTeleportHelper.getPlayerList()) {
                    randomTeleportHelper.sendMessagePlayers("&8- &d" + players.getName());

                    for (PotionEffect effect : players.getActivePotionEffects())
                        players.removePotionEffect(effect.getType());
                }
                if (this.sectorService.getCurrentSector().get().isSpawn()) {
                    randomTeleportHelper.teleport();
                }
            });
        }
    }
    public static void randomTp(Player player) {
        int x = RandomUtilities.getRandInt(-1500, 1500);
        int z = RandomUtilities.getRandInt(-1500, 1500);
        double y = player.getWorld().getHighestBlockYAt(x, z) + 1.5f;
        Location loc = new Location(player.getWorld(), x, y, z);
        player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
