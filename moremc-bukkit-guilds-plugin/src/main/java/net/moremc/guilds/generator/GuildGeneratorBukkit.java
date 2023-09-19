package net.moremc.guilds.generator;

import org.bukkit.Material;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.bukkit.api.generator.GeneratorRegion;
import net.moremc.bukkit.api.generator.RegionGenerator;

public class GuildGeneratorBukkit extends RegionGenerator {

    public GuildGeneratorBukkit(GeneratorRegion region, GuildGenerator guildGenerator, Material material, GuildImpl guild) {
        super(region, guildGenerator, material, guild);
    }
}
