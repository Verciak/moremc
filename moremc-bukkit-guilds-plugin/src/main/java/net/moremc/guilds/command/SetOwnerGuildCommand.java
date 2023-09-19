package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class SetOwnerGuildCommand extends GuildSubCommand {

    private final GuildService guildService = API.getInstance().getGuildService();

    public SetOwnerGuildCommand() {
        super("zalozyciel", "owner");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        MessageHelper messageHelper = new MessageHelper(player, "");


        if (args.length < 1) {
            messageHelper.setMessage(MessageHelper.colored("&7Poprawne użycie&8: &d{COMMAND}")
                    .replace("{COMMAND}", "/g zalozyciel <nick>")).send(SendMessageType.CHAT);
            return false;
        }

        if (!this.guildService.findGuildByNickName(player.getName()).isPresent()) {
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }
        String nickName = args[0];
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {


            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {
                if(!GuildPlayerType.hasPermissionOwner(guildPlayer.getPlayerType())){
                    messageHelper.setMessage(MessageHelper.colored("&cTylko założyciel gildii może zmieniać zarząd.")).send(SendMessageType.CHAT);
                    return;
                }
                if (nickName.equalsIgnoreCase(player.getName())) {
                    messageHelper.setMessage(MessageHelper.colored("&cNie możesz zmieniać roli samemu sobie.")).send(SendMessageType.CHAT);
                    return;
                }
                if(!guild.findGuildPlayerByNickname(nickName).isPresent()){
                    messageHelper.setMessage(MessageHelper.colored("&cPodany gracz nie jest w twojej gildii")).send(SendMessageType.CHAT);
                    return;
                }
                guild.findGuildPlayerByNickname(nickName).ifPresent(guildPlayerTarget -> {
                    guildPlayer.setPlayerType(GuildPlayerType.MEMBER);
                    guild.setOwnerNickname(guildPlayerTarget.getNickName());
                    guildPlayerTarget.setPlayerType(GuildPlayerType.OWNER);
                    guild.sendMessage(MessageHelper.colored("&fZałożyciel gildii nadał nowego założyciela gildii&8: &a{PLAYER}")
                            .replace("{PLAYER}", nickName));
                    guild.synchronize(SynchronizeType.UPDATE);
                });
            });
        });
        return false;
    }
}
