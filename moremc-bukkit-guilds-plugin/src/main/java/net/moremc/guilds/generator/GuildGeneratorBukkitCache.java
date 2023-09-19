package net.moremc.guilds.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuildGeneratorBukkitCache {

    private final List<GuildGeneratorBukkit> guildGeneratorBukkitList = new ArrayList<>();

    public Optional<GuildGeneratorBukkit> findGuildGeneratorByName(String guildName){
        return this.guildGeneratorBukkitList
                .stream()
                .filter(guildGeneratorBukkit -> guildGeneratorBukkit.getGuild().getName().equalsIgnoreCase(guildName))
                .findFirst();
    }

    public List<GuildGeneratorBukkit> getGuildGeneratorBukkitList() {
        return guildGeneratorBukkitList;
    }
}
