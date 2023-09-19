package net.moremc.bukkit.tools.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.sector.Sector;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SpawnScoreboard implements AssembleAdapter
{

    private final Optional<Sector> sector = API.getInstance().getSectorService().getCurrentSector();

    @Override
    public String getTitle(Player player) {
        return MessageHelper.colored("&5&m-> &8 &d&lPAY&f&lMC &5&m <-");
    }
    @Override
    public List<String> getLines(Player player) {
        return MessageHelper.colored(Arrays.asList(
                "  ",
                "&fZnajdujesz się na&8:",
                "       &d" + sector.get().getName(),
                "   ",
                "&fGraczy&8: &d" + sector.get().getInfo().getPlayerList().size() + "&8/&5500",
                "&fTPS&8: " + (sector.get().getInfo().getTicksPerSeconds() > 20 ? "&a20*" : "&a" + new DecimalFormat("##.##").format(sector.get().getInfo().getTicksPerSeconds())),
                "      ",
                "&fAby zminić sektor wpisz",
                "        &d/ch",
                "         ",
                "       &7p a y m c . p l"
        ));
    }
}