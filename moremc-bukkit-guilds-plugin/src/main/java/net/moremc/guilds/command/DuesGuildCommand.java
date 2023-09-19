package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;
import net.moremc.guilds.inventory.dues.GuildDuesSelectInventory;

public class DuesGuildCommand extends GuildSubCommand {

    private final GuildDuesSelectInventory duesSelectInventory = new GuildDuesSelectInventory();
    private final GuildService guildService = API.getInstance().getGuildService();

    public DuesGuildCommand() {
        super("skladki", "skladka", "dues");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            this.duesSelectInventory.show(player);
        });
        return false;
    }
}
