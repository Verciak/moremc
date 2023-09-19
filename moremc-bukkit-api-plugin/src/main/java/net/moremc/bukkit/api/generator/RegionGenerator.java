package net.moremc.bukkit.api.generator;

import org.bukkit.Material;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.entity.guild.impl.GuildImpl;

public class RegionGenerator extends Generator {


  public RegionGenerator(GeneratorRegion region, GuildGenerator guildGenerator, Material material) {
    super(material, new RegionGeneratorFiller(guildGenerator, region, material));
  }

  public RegionGenerator(GeneratorRegion region,GuildGenerator guildGenerator, Material material, GuildImpl guild) {
    super(region, new RegionGeneratorFiller(guildGenerator, region, material), material, guild);
  }
}
