package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;
import net.moremc.guilds.inventory.create.GuildCreateConfigurationInventory;

public class ConfigurationGuildCommand extends GuildSubCommand {


    private final GuildService guildService = API.getInstance().getGuildService();
    private final GuildCreateConfigurationInventory createConfigurationInventory = new GuildCreateConfigurationInventory();

    public ConfigurationGuildCommand() {
        super("konfiguracja");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            MessageHelper messageHelper = new MessageHelper(player, "");


            if (!API.getInstance().getSectorService().getCurrentSector().get().isSpawn()) {
                messageHelper.setMessage(MessageHelper.translateText("&cGilide można skonfigurować tylko na spawnie.")).send(SendMessageType.TITLE);
                return;
            }

            if(guild.getLocation() != null){
                messageHelper.setMessage(MessageHelper.colored(
                        "&cTwoja gildia już została skonfigurowana.")).send(SendMessageType.TITLE);
                return;
            }
            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {
                if(!guildPlayer.getPlayerType().equals(GuildPlayerType.OWNER)){
                    messageHelper.setMessage(MessageHelper.translateText("&cNie jesteś założycielem gildii.")).send(SendMessageType.TITLE);
                    return;
                }
                this.createConfigurationInventory.show(player);
            });

        });
        return false;
    }
}
