package net.moremc.guilds.command;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

import java.util.ArrayList;
import java.util.List;

public class InfoGuildCommand extends GuildSubCommand {

    private final GuildService guildService = API.getInstance().getGuildService();

    public InfoGuildCommand() {
        super("info");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        MessageHelper messageHelper = new MessageHelper(player, "");

        if (args.length < 1) {
            messageHelper.setMessage(MessageHelper.colored("&7Poprawne użycie: &d{COMMAND}")
                    .replace("{COMMAND}", "/g info <tag>")).send(SendMessageType.CHAT);
            return false;
        }
        String nameTagGuild = args[0].toUpperCase();
        if (!this.guildService.findByValueOptional(nameTagGuild).isPresent()) {
            messageHelper.setMessage(MessageHelper.colored("&4Błąd: &cPodana gildia nie istnieje.")).send(SendMessageType.CHAT);
            return false;
        }
        this.guildService.findByValueOptional(nameTagGuild).ifPresent(guild -> {
            List<String> messageList = new ArrayList<>();
            messageList.add(" ");
            messageList.add(" &fNazwa: &d" + guild.getName());
            messageList.add(" &fPełna nazwa: &d" + guild.getFullName());
            messageList.add(" &fZałożyciel: &d" + guild.getOwnerNickname());
            messageList.add(" &fZastępcy: &d" + guild.getGuildLeaderList().replace("[", "").replace("]", ""));
            messageList.add(" &fMistrzowie: &d" + guild.getGuildMasterList().replace("[", "").replace("]", ""));
            messageList.add(" &fPunkty: &d" + guild.getStatics().getPoints());
            messageList.add(" &fZabójstwa: &a" + guild.getStatics().getKills());
            messageList.add(" &fZgony: &c" + guild.getStatics().getDeaths());
            messageList.add(" &fKordynaty: &fX: &a" + (guild.getLocation() == null ? "&cNie wybrano." : guild.getLocation().getX()) + "&8, &fZ: &a" + (guild.getLocation() == null ? "&cNie wybrano." : guild.getLocation().getZ()));
            messageList.add(" &fWygasa za: &d" + DataHelper.getTimeToString(guild.getExpireTime()));
            messageList.add(" &fOchrona: &a" + (guild.getProtectionTime() <= System.currentTimeMillis() ? "&cNIE&7, &c0sek" : "&aTAK&7, &a" + DataHelper.getTimeToString(guild.getProtectionTime())));
            messageList.add(" &fCzłonkowie&8(&a" + guild.getOnlinePlayersCount() + "&7/&2" + guild.getPlayerMap().values().size() + "&8)&7, " + guild.getOnlinePlayers());
            messageList.add(" ");

            for (String message : messageList) {
                player.sendMessage(MessageHelper.colored(message));
            }

        });

        return false;
    }
}
