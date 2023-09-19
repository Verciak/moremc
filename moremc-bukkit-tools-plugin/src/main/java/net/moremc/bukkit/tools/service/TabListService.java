package net.moremc.bukkit.tools.service;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.user.User;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.cache.entity.BukkitUser;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.type.SendType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class TabListService {

    private final UserService userService = API.getInstance().getUserService();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final SectorService sectorService = API.getInstance().getSectorService();


    public void update(Optional<BukkitUser> bukkitUserOptional) {
        bukkitUserOptional.ifPresent(bukkitUser -> {
            bukkitUser.getTablistHelper().apply(tablistHelper -> {
                User user = userService.findByValue(bukkitUser.getName());

                tablistHelper.setTexture("ewogICJ0aW1lc3RhbXAiIDogMTY1MjAzMjk5MDEzNywKICAicHJvZmlsZUlkIiA6ICIzZDFjMTA3YWE4NWI0NDA4OGE2NmJiNjczNjNhZjYxZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJMb2ciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTVhNzhlYTM1YzY5YmI3Y2FkMWRiOWRlZDllYzIzODgyNDk5NzBkMTA5NDJkNTRjNTNhZDgxYzQ4ODZlNTU3MyIKICAgIH0KICB9Cn0=");
                tablistHelper.setSignature("PnrWnivdBk+LRrAz8PpcDdCDhZ31F5rwcKyx6hCOhS8AEdG3nBJg+EYVLVG+jSeOclZMv/tRL3pY2nr/c6RbU1irSEEPo11bv9U79TPgVokk/Cw6buIjsF9qtWG5VA2ras4vHDgkDcIRtfZjWp8mpg0M9Y33dSRZVeox0YG6l5Ywo5X3uWqAmslJjaL4VqkQqEIAC75ffYkHF5pYUaoQcKtWj5nj/6c71W3eJNbBas7mOtQgwL02WWRThMPso7Rz7P0a5EMlrv32954UM8ZCki6z2KFnLckZbKv4rLjF7ze0D1Kxc6nHtwVTGRtQ/fDWez2uvEYZG3nL01hoP6tB/8N5y7i96sGNPm3Q49JmLBgCzm7zs5TjFzsoxSov3ENNfyI3rHWEDg0Wjgk+H9q8yFwbQO4gJGJqGwe49Bx7vcvPFATA6MOJ99AeJcpYuF2HXmsHm9qjWkSCSWNPzfpAW25XJf6/4HdWcz1Cc+rSCq9iifLskrcebqzkpESBG8+rDJypVUkj+uPnFHG12MlQFZGj8iuQPMuJcMxHpdiqeM6V32NbGKc3ULVrMyjfTSFbedXX5dWfvcxGpEBEeWRCqSYvl0didcH6drLaRNKDAI2F0O9kb2Qz6XJga2efQ5eC5cHHwL2SNoWl7KkO2WER3RTXA3wD8F3R4ts/hYcXTKY=");
                tablistHelper.setHeader(MessageHelper.colored(Arrays.asList(
                        "             &d&m-----> &8 &5&lPAY&f&lMC &d&m <-----",
                        "&fGraczy online &d" + sectorService.getOnline() + "&8/&52000"
                )));

                List<User> userSortedList = new ArrayList<>(this.userService.getMap().values());

                userSortedList.sort((userOne, userTwo) -> {
                    Integer userOnePoints = userOne.getUserStatics().getPoints();
                    Integer userTwoPoints = userTwo.getUserStatics().getPoints();
                    return userTwoPoints.compareTo(userOnePoints);
                });
                List<String> rankingList = new ArrayList<>();
                IntStream.range(1, 17).forEach(i -> {
                    if(userSortedList.size() >= i) {
                        User userSorted = userSortedList.get(i - 1);
                        rankingList.add("&7 " + i + ". &f" + userSorted.getNickName() + " &8[&d" + userSorted.getUserStatics().getPoints() + "&8]" + (this.sectorService.isOnlinePlayer(userSorted.getNickName()) ? " &a■" : " &c■") );
                    }else{
                        rankingList.add("&7 " + i + ". &dBrak");
                    }
                });

                List<GuildImpl> guildSortedList = new ArrayList<>(this.guildService.getMap().values());

                guildSortedList.sort((guildOne, guildTwo) -> {
                    Integer guildOnePoints = guildOne.getStatics().getPoints();
                    Integer guildTwoPoints = guildTwo.getStatics().getPoints();
                    return guildTwoPoints.compareTo(guildOnePoints);
                });
                List<String> rankingGuildList = new ArrayList<>();
                IntStream.range(1, 17).forEach(i -> {
                    if(guildSortedList.size() >= i) {
                        GuildImpl guildSorted = guildSortedList.get(i - 1);
                        rankingGuildList.add("&f" + i + ". &d" + guildSorted.getName().toUpperCase() + " &8[&5" + guildSorted.getStatics().getPoints() + "&8]");
                    }else{
                        rankingGuildList.add("&f" + i + ". &dBrak");
                    }
                });

                List<String> tablistStringList = new ArrayList<>();
                tablistStringList.add("");
                tablistStringList.add("        &5&lTOPOWI GRACZE");
                tablistStringList.add("");

                tablistStringList.addAll(rankingList);
                Optional<GuildImpl> guild = this.guildService.findGuildByNickName(bukkitUser.getName());


                tablistStringList.add("");
                tablistStringList.add("");
                tablistStringList.add("        &5&lTOPOWE GILDIE");
                tablistStringList.add("");
                tablistStringList.addAll(rankingGuildList);
                tablistStringList.add("");
                tablistStringList.add("");
                tablistStringList.add("     &5&lTWOJE STATYSTYKI");
                tablistStringList.add("");
                tablistStringList.add(" &fNick: &d" + user.getNickName());
                tablistStringList.add(" &fRanga: " + user.getGroupType().getPrefix());
                tablistStringList.add(" &fRanking: &d" + user.getUserStatics().getPoints());
                tablistStringList.add(" &fZabójstwa: &d" + user.getUserStatics().getKills());
                tablistStringList.add(" &fŚmierci: &d" + user.getUserStatics().getDeaths());
                tablistStringList.add(" &fAsysty: &d" + user.getUserStatics().getAssists());
                tablistStringList.add(" &fK/D: &d0");
                tablistStringList.add("");
                tablistStringList.add("   &8* &5&lINFORMAJCE &8*");
                tablistStringList.add("");
                tablistStringList.add(" &fSektor: &d" + API.getInstance().getSectorService().getCurrentSector().get().getName());
                tablistStringList.add(" &fProxy: &dproxy01");
                tablistStringList.add("");
                tablistStringList.add(" &fIncognito: " + (user.isIncognito() ? "&awlaczone" : "&cwylaczone"));
                tablistStringList.add(" &fPING: &d" + ((CraftPlayer) Bukkit.getPlayer(user.getNickName())).getHandle().ping);
                tablistStringList.add("");
                tablistStringList.add("");
                tablistStringList.add("   &8* &5&lGILDIA &8*");
                tablistStringList.add("");
                tablistStringList.add(" &fTAG: &d" + (guild.map(value -> value.getName().toUpperCase()).orElse("&5Wczytywanie...")));
                tablistStringList.add(" &fNazwa: &d" + (!guild.isPresent() ? "&5Wczytywanie..." : guild.get().getFullName()));
                tablistStringList.add(" &fRanking: &d" + (!guild.isPresent() ? "&5Wczytywanie..." : guild.get().getStatics().getPoints()));
                tablistStringList.add(" &fZabójstwa: &d" + (!guild.isPresent() ? "&5Wczytywanie..." : guild.get().getStatics().getKills()));
                tablistStringList.add(" &fŚmierci: &d" + (!guild.isPresent() ? "&5Wczytywanie..." : guild.get().getStatics().getDeaths()));
                tablistStringList.add(" ");
                tablistStringList.add(" &fPVP: " + (guild.map(value -> value.isFriendlyFire() ? "&aTAK" : "&cNIE").orElse("&5Wczytywanie...")));
                tablistStringList.add(" &fOchrona: " + (!guild.isPresent() ? "&5Wczytywanie..." : (!guild.get().hasProtection() ? "&aTAK&7, &a" + DataHelper.getTimeToString(guild.get().getProtectionTime()) : "&cNIE&7, &c0sek")));
                tablistStringList.add(" &fWygasa: &d" + (!guild.isPresent() ? "&5Wczytywanie..." : DataHelper.getTimeToString(guild.get().getExpireTime())));
                tablistStringList.add("");
                tablistStringList.add(" &fChcesz zarządzać gildią?");
                tablistStringList.add("         &fWpisz&8: &d/g panel");
                tablistStringList.add("");
                tablistStringList.add(" &fInformację na temat gildii&8:");
                tablistStringList.add("           &d/gildia");
                tablistStringList.add("");

                tablistHelper.setFooter(MessageHelper.translateText(Arrays.asList(
                        "&fUkrywaj skina&7,&fnick&7,&frange pod komendą&8(&d/incognito&8)",
                        "&fMożesz edytować ustawienia czatu&8(&d/cc&8)",
                        "&8* &fSklep: &dpaymc.pl &8* &fTS: &dts.paymc.pl &8* &fFB: &dpaymc.pl/fb"
                )));
                tablistHelper.setCells(MessageHelper.translateText(tablistStringList));
            }).send(SendType.UPDATE);
        });
    }
}