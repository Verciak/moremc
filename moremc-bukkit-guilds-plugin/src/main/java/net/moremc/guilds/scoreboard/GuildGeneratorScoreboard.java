package net.moremc.guilds.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.moremc.guilds.utilities.GuildMagazineItemsUtilities;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.entity.guild.regeneration.GuildRegenerationType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GuildGeneratorScoreboard implements AssembleAdapter {

    private final GuildService guildService = API.getInstance().getGuildService();

    @Override
    public String getTitle(Player player) {
        return MessageHelper.colored("&5&m-> &8 &d&lPAY&f&lMC &5&m <-");
    }

    @Override
    public List<String> getLines(Player player) {
        AtomicReference<List<String>> stringList = new AtomicReference<>();

        this.guildService.findGuildByLocation(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ()).ifPresent(guild -> {

            if(guild.getRegeneration().getRegenerationType().equals(GuildRegenerationType.START)){
                return;
            }

            if(guild.hasPlayer(player.getName()) != null){
               guild.findGuildGeneratorIsActive().ifPresent(guildGenerator -> {
                   stringList.set(MessageHelper.colored(Arrays.asList(
                           "  ",
                           "&fWykładanie " + (guildGenerator.getGeneratorType().equals(GuildGeneratorType.SAND) ? "piasku" : guildGenerator.getGeneratorType().equals(GuildGeneratorType.OBSIDIAN) ? "obsydianu" : guildGenerator.getGeneratorType().equals(GuildGeneratorType.AIR) ? "powietrza" : ""),
                           "   ",
                           "&fPominięto&8: &c" + guildGenerator.getSkippedBlocks(),
                           "&fWygenerowano&8: &d" + guildGenerator.getSuccessBlocks(),
                           "&fMateriały&8: &d" + GuildMagazineItemsUtilities.countMaterial(guildGenerator),
                           "&fPozostało&8: &d" + DataHelper.getTimeToString(guildGenerator.getActiveTime()),
                           "      ",
                           "       &7m o r e m c . e u")));
               });
            }
        });
        return stringList.get();
    }
}
