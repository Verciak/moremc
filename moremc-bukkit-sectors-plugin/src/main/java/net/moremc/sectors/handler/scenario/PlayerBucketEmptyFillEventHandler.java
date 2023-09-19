package net.moremc.sectors.handler.scenario;

import net.moremc.api.API;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class PlayerBucketEmptyFillEventHandler implements Listener {


    private final SectorService sectorService = API.getInstance().getSectorService();

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event){
        if(event.isCancelled())return;

        Player player = event.getPlayer();
        Block block = event.getBlockClicked();

        this.sectorService.findSectorByLocation(player.getWorld().getName(), block.getX(), block.getZ()).ifPresent(sector -> {
            if(sector.getInfo().getLocationInfo().getDistanceToBorder(block.getX(), block.getZ()) <= 20){
                event.setCancelled(true);
                player.sendMessage(MessageHelper.colored("&4Błąd: &cTa interakcja przy sektorze zablokowana."));
            }
        });
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketFill(PlayerBucketFillEvent event){
        if(event.isCancelled())return;

        Player player = event.getPlayer();
        Block block = event.getBlockClicked();

        this.sectorService.findSectorByLocation(player.getWorld().getName(), block.getX(), block.getZ()).ifPresent(sector -> {
            if(sector.getInfo().getLocationInfo().getDistanceToBorder(block.getX(), block.getZ()) <= 20){
                event.setCancelled(true);
                player.sendMessage(MessageHelper.colored("&4Błąd: &cTa interakcja przy sektorze zablokowana."));
            }
        });
    }
}
