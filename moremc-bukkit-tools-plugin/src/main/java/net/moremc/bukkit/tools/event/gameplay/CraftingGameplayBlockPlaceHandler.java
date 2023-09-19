package net.moremc.bukkit.tools.event.gameplay;

import net.moremc.bukkit.tools.event.crafting.CraftingGameplayBlockPlaceEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.function.Consumer;

public class CraftingGameplayBlockPlaceHandler implements Consumer<CraftingGameplayBlockPlaceEvent> {


    @Override
    public void accept(CraftingGameplayBlockPlaceEvent event) {

        Player player = event.getPlayer();
        BlockPlaceEvent blockPlaceEvent = event.getBlockPlaceEvent();
        Block block = blockPlaceEvent.getBlock();

        if(block.getType().equals(Material.ENDER_STONE)){
            Block blockUpperOne = block.getLocation().add(0.0, 1.0, 0.0).getBlock();
            Block blockUpperTwo = block.getLocation().add(0.0, 2.0, 0.0).getBlock();
            blockUpperOne.setType(Material.STONE);
            blockUpperTwo.setType(Material.STONE);

            player.playEffect(block.getLocation(), org.bukkit.Effect.MOBSPAWNER_FLAMES, 50);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
            player.sendTitle(MessageHelper.translateText("&dSTOWNIARKA"), MessageHelper.translateText("&aPomyślnie postawiono."));
        }
    }
}
