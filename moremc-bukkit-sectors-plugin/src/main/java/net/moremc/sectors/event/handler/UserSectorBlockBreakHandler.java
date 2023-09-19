package net.moremc.sectors.event.handler;

import net.moremc.api.API;
import net.moremc.api.service.entity.SectorService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.sectors.event.UserBlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.function.Consumer;

public class UserSectorBlockBreakHandler implements Consumer<UserBlockBreakEvent> {

    private final SectorService sectorService = API.getInstance().getSectorService();

    @Override
    public void accept(UserBlockBreakEvent userBlockBreakEvent) {
        BlockBreakEvent event = userBlockBreakEvent.getBlockBreakEvent();
        Player player = event.getPlayer();

        userBlockBreakEvent.getOptionalUser().ifPresent(user -> {
            if(!user.getUserSector().isCanInteract()){
                event.setCancelled(true);
                return;
            }
            Block block = event.getBlock();
            this.sectorService.findSectorByLocation(player.getWorld().getName(), block.getX(), block.getZ()).ifPresent(sector -> {
                if(sector.getInfo().getLocationInfo().getDistanceToBorder(block.getX(), block.getZ()) <= 20){
                    event.setCancelled(true);
                    player.sendMessage(MessageHelper.colored("&4Błąd: &cTa interakcja przy sektorze zablokowana."));
                }
            });
        });
    }
}
