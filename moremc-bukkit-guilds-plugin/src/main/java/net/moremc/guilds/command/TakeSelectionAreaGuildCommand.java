package net.moremc.guilds.command;

import net.moremc.api.API;
import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import net.moremc.guilds.command.sub.GuildSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TakeSelectionAreaGuildCommand extends GuildSubCommand {

    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();

    public TakeSelectionAreaGuildCommand() {
        super("zajmij", "take");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }
        int amountGold = ItemUtilities.getAmountOf(player, Material.GOLD_INGOT, (short) 0);

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            if(guild.getLocation() != null){
                messageHelper.setMessage(MessageHelper.colored(
                        "&cTwoja gildia już została skonfigurowana.")).send(SendMessageType.TITLE);
                return;
            }

            if(amountGold < 64){
                messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz wystarczającej liczby złota.")).send(SendMessageType.TITLE);
                return;
            }

            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                GuildArea guildArea = user.getGuildAreaSelect();
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 64));
                player.teleport(new Location(Bukkit.getWorld("world"), guildArea.getX(), 74, guildArea.getZ()));
                player.sendTitle("", MessageHelper.translateText("&fPomyślnie &azakupiono &fszybki teleport."));
            });

        });

        return false;
    }
}
