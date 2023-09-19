package net.moremc.bukkit.api.generator;

import org.bukkit.Material;
import net.moremc.api.entity.guild.impl.GuildImpl;

public class Generator {

  private GeneratorRegion generatorRegion;
  protected final Material material;
  private final Filler filler;
  private final RegionGeneratorFiller regionGeneratorFiller;
  private GuildImpl guild;

  public Generator(Material material,Filler filler) {
    this.material = material;
    this.filler = filler;
    this.regionGeneratorFiller = (RegionGeneratorFiller) filler;
  }

  public Generator(GeneratorRegion region, Filler filler,  Material material, GuildImpl guild) {
    this.generatorRegion = region;
    this.material = material;
    this.filler = filler;
    this.regionGeneratorFiller = (RegionGeneratorFiller) filler;
    this.guild = guild;
  }
  public GeneratorRegion getGeneratorRegion() {
    return generatorRegion;
  }

  public GuildImpl getGuild() {
    return guild;
  }

  public void start() {
    this.filler.fill();
  }

  public void forceFinish() {
    this.filler.completeNow();
  }

  public Material getMaterial() {
    return material;
  }

  public Filler getFiller() {
    return filler;
  }

  public RegionGeneratorFiller getRegionGeneratorFiller() {
    return regionGeneratorFiller;
  }
}