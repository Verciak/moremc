package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;
import net.moremc.guilds.inventory.GuildPanelInventory;

public class PanelGuildCommand extends GuildSubCommand {


    private final GuildPanelInventory panelInventory = new GuildPanelInventory();
    private final GuildService guildService = API.getInstance().getGuildService();

    public PanelGuildCommand() {
        super("panel");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            this.panelInventory.show(player);
        });

        return false;
    }
}
