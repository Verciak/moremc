package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;
import net.moremc.guilds.inventory.GuildTreasureInventory;

public class TreasureGuildCommand extends GuildSubCommand {

    private final GuildTreasureInventory treasureInventory = new GuildTreasureInventory();
    private final GuildService guildService = API.getInstance().getGuildService();

    public TreasureGuildCommand() {
        super("skarbiec", "treasure");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {

                if(!guildPlayer.hasPermission(guild, 34)){
                    messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej akcji poproś kogoś w gildii.")).send(SendMessageType.CHAT);
                    return;
                }
                if(guild.getTreasure().isOpen()){
                    player.sendTitle("", MessageHelper.colored("&cAktualnie ktoś przebywa w skarbcu gildyjnym."));
                    return;
                }
                this.treasureInventory.show(player);
            });
        });
        return false;
    }
}
