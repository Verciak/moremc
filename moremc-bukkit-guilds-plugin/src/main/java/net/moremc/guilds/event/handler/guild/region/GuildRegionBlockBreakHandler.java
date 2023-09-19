package net.moremc.guilds.event.handler.guild.region;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import net.moremc.api.API;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.data.guild.GuildPermissionDataArray;
import net.moremc.api.data.guild.type.GuildPermissionActionDataType;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import net.moremc.guilds.event.guild.region.GuildRegionBlockBreakEvent;

import java.util.function.Consumer;

public class GuildRegionBlockBreakHandler implements Consumer<GuildRegionBlockBreakEvent> {

    private final UserService userService = API.getInstance().getUserService();
    private final GuildPermissionData permissionData = CommunicatorPlugin.getInstance().getPermissionData();

    @Override
    public void accept(GuildRegionBlockBreakEvent event) {
        Player player = event.getPlayer();
        BlockBreakEvent blockBreakEvent = event.getBlockBreakEvent();
        GuildImpl guild = event.getGuild();


        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if(guild.hasPlayer(player.getName()) == null && !UserGroupType.hasPermission(UserGroupType.ADMIN, user)){
                blockBreakEvent.setCancelled(true);
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cTeren należy do wrogiej gildii&8[&4" + guild.getName().toUpperCase() + "&8]"));
                return;
            }
            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {
                MessageHelper messageHelper = new MessageHelper(player, "");

                boolean status = true;
                for (GuildPermissionDataArray permissionDataArray : this.permissionData.getPermissionDataArrays()) {
                    if(permissionDataArray.getActionType().equals(GuildPermissionActionDataType.BREAK)) {
                        if (!permissionDataArray.getMaterialNameOrUrl().equalsIgnoreCase(blockBreakEvent.getBlock().getType().name())) continue;
                        if (!guildPlayer.hasPermission(guild, permissionDataArray.getId())) {
                            status = false;
                        }
                    }
                }
                if(!status) {
                    blockBreakEvent.setCancelled(true);
                    messageHelper.setMessage("&4Błąd: &cNie posiadasz dostępu do tej akcji poproś kogoś w gildii.").send(SendMessageType.CHAT);
                }
            });
        });

    }
}
