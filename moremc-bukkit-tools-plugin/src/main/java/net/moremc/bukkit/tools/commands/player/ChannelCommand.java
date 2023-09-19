package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.sector.Sector;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.sector.ChannelInventory;

import java.util.Optional;


public class ChannelCommand
{
  private final ToolsPlugin plugin;
  private final API api;

  public ChannelCommand(ToolsPlugin plugin) {
    this.plugin = plugin;
    this.api = API.getInstance();
  }

  @Command(value = {"channel", "ch"}, description = "Otwiera GUI z dostępnymi knałami spawna.")
  public void handle(@Sender Player player) {
    Optional<Sector> sector = api.getSectorService().getCurrentSector();

    sector.ifPresent(consumer -> {
      if(!consumer.isSpawn()) {
        player.sendMessage(MessageHelper.colored("&cNie zanjdujesz się na sektorze spawn aby to zrobić!"));
        return;
      }
      new ChannelInventory().show(player);
    });
  }
}
