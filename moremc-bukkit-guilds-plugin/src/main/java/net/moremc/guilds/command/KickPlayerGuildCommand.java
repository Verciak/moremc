package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class KickPlayerGuildCommand extends GuildSubCommand {


    private final GuildService guildService = API.getInstance().getGuildService();

    public KickPlayerGuildCommand() {
        super("wyrzuc", "kick");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");


        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }
        if (args.length < 1) {
            messageHelper.setMessage(MessageHelper.colored("&7Poprawne użycie&8: &d{COMMAND}")
                    .replace("{COMMAND}", "/g wyrzuc <nick>")).send(SendMessageType.CHAT);
            return false;
        }
        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            String nickName = args[0];
            if(!guild.findGuildPlayerByNickname(nickName).isPresent()){
                messageHelper.setMessage(MessageHelper.colored("&cPodany gracz nie jest w twojej gildii")).send(SendMessageType.CHAT);
                return;
            }
            if(nickName.equalsIgnoreCase(player.getName())){
                messageHelper.setMessage(MessageHelper.colored("&cNie możesz wyrzucić samego siebie.")).send(SendMessageType.CHAT);
                return;
            }
            guild.findGuildPlayerByNickname(nickName).ifPresent(guildPlayer -> {
                guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayerSender -> {

                    if(!guildPlayerSender.hasPermission(guild, 33)){
                        messageHelper.setMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej akcji poproś kogoś w gildii.")).send(SendMessageType.CHAT);
                        return;
                    }

                    if(guildPlayer.getPlayerType().equals(GuildPlayerType.OWNER)){
                        messageHelper.setMessage(MessageHelper.colored("&cNie możesz wyrzucić założyciela gildii."));
                        return;
                    }
                    if(!guildPlayerSender.getPlayerType().equals(GuildPlayerType.OWNER) && guildPlayer.getPlayerType().equals(GuildPlayerType.LEADER) || guildPlayer.getPlayerType().equals(GuildPlayerType.MASTER)){
                        messageHelper.setMessage(MessageHelper.colored("&cTylko założyciel gildii może wyrzucić zastępce lub mistrza.")).send(SendMessageType.CHAT);
                        return;
                    }
                    guild.removePlayer(guildPlayer);
                    player.sendTitle("", MessageHelper.translateText("&cPomyślnie wyrzucono gracza&8: &4" + guildPlayer.getNickName()));

                    ClientSendMessagePacket packet = new ClientSendMessagePacket("&c&l♚ &fGracz&8: &c" + nickName + " &fzostał wyrzucony z gildii&8[&c" + guild.getName() + "&8]",
                            SendMessageReceiverType.ALL, SendMessageType.CHAT);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", packet);

                    guild.synchronize(SynchronizeType.UPDATE);
                });
            });

        });

        return false;
    }
}
