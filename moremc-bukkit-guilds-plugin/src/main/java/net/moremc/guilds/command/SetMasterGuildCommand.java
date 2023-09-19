package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

public class SetMasterGuildCommand extends GuildSubCommand {


    private final GuildService guildService = API.getInstance().getGuildService();

    public SetMasterGuildCommand() {
        super("mistrz", "master", "ustawmistrza");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");

        if(!this.guildService.findGuildByNickName(player.getName()).isPresent()){
            messageHelper.setMessage(MessageHelper.colored( "&cNie posiadasz gildii.")).send(SendMessageType.CHAT);
            return false;
        }

        if(args.length < 2){
            messageHelper.setMessage(MessageHelper.colored("&7Poprawne użycie&8: &d{COMMAND}")
                    .replace("{COMMAND}", "/g mistrz <ustaw/usun> <nick>")).send(SendMessageType.CHAT);
            return false;
        }

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {

            guild.findGuildPlayerByNickname(player.getName()).ifPresent(guildPlayer -> {

                if(!GuildPlayerType.hasPermissionOwner(guildPlayer.getPlayerType())){
                    messageHelper.setMessage(MessageHelper.colored("&cTylko założyciel gildii może zmieniać zarząd.")).send(SendMessageType.CHAT);
                    return;
                }

                String nickName = args[1];
                if(!guild.findGuildPlayerByNickname(nickName).isPresent()){
                    messageHelper.setMessage(MessageHelper.colored("&cPodany gracz nie jest w twojej gildii")).send(SendMessageType.CHAT);
                    return;
                }
                if(nickName.equalsIgnoreCase(player.getName())){
                    messageHelper.setMessage(MessageHelper.colored("&cNie możesz zmieniać roli samemu sobie.")).send(SendMessageType.CHAT);
                    return;
                }
                guild.findGuildPlayerByNickname(nickName).ifPresent(guildPlayerTarget -> {
                    switch (args[0]){
                        case "ustaw":{
                            if(guild.getCountMaster() >= 3){
                                messageHelper.setMessage(MessageHelper.colored("&cTwoja gildia posiada już &43 &cmistrzów w gildii")).send(SendMessageType.CHAT);
                                return;
                            }
                            if(guildPlayerTarget.getPlayerType().equals(GuildPlayerType.MASTER)){
                                messageHelper.setMessage(MessageHelper.colored("&cPodany członek jest już mistrzem")).send(SendMessageType.CHAT);
                                return;
                            }
                            guildPlayerTarget.setPlayerType(GuildPlayerType.MASTER);
                            guild.sendMessage(MessageHelper.colored("&fZałożyciel gildii nadał nowego mistrza gildii &a{PLAYER}")
                                    .replace("{PLAYER}", nickName));
                            guild.synchronize(SynchronizeType.UPDATE);
                            break;
                        }
                        case "usun":{
                            if(!guildPlayerTarget.getPlayerType().equals(GuildPlayerType.MASTER)){
                                messageHelper.setMessage(MessageHelper.colored("&cPodany gracz nie jest mistrzem w gildii")).send(SendMessageType.CHAT);
                                return;
                            }
                            guildPlayerTarget.setPlayerType(GuildPlayerType.MEMBER);
                            guild.sendMessage(MessageHelper.colored("&fZałożyciel gildii usunał mistrza gildii &d{PLAYER}")
                                    .replace("{PLAYER}", nickName));
                            guild.synchronize(SynchronizeType.UPDATE);
                            break;
                        }
                    }
                });
            });


        });

        return false;
    }
}
