package net.moremc.guilds.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.regeneration.GuildRegenerationType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GuildRegenerationScoreboard implements AssembleAdapter {

    private final GuildService guildService = API.getInstance().getGuildService();

    @Override
    public String getTitle(Player player) {
        return MessageHelper.colored("&5&m-> &8 &d&lPAY&f&lMC &5&m <-");
    }

    @Override
    public List<String> getLines(Player player) {

        AtomicReference<List<String>> stringList = new AtomicReference<>();
        this.guildService.findGuildByLocation(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ()).ifPresent(guild -> {

            if (guild.findGuildGeneratorIsActive().isPresent()) {
                return;
            }
            if(!guild.getRegeneration().getRegenerationType().equals(GuildRegenerationType.START)){
                return;
            }
            if (guild.hasPlayer(player.getName()) != null) {
                stringList.set(MessageHelper.colored(Arrays.asList(
                        "  ",
                        "&fTrwa regeneracja gildii&8:",
                        "   ",
                        "&fBloków do końca&8: &d" + guild.getRegeneration().getBlockStateList().size(),
                        "&fPozostało&8: &d" + DataHelper.getCringeTimeToString(System.currentTimeMillis() - guild.getRegeneration().getTimeLeft()),
                        "      ",
                        "       &7m o r e m c . e u")));
            }
        });
        return stringList.get();
    }
}
