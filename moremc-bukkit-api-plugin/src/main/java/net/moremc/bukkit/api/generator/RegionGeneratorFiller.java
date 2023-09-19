package net.moremc.bukkit.api.generator;

import net.moremc.api.API;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.bukkit.api.BukkitAPI;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class RegionGeneratorFiller implements Filler {

  private final GuildGenerator guildGenerator;
  private final GeneratorRegion region;
  private final Material material;
  private int count = 0;

  public RegionGeneratorFiller(GuildGenerator guildGenerator, GeneratorRegion region, Material material) {
    this.guildGenerator = guildGenerator;
    this.region = region;
    this.material = material;
  }

  public GuildGenerator getGuildGenerator() {
    return guildGenerator;
  }

  public GeneratorRegion getRegion() {
    return region;
  }

  public Material getMaterial() {
    return material;
  }

  @Override
  public void fill() {
    int area = this.region.area();

    AtomicInteger index = new AtomicInteger();

    new BukkitRunnable() {
      @Override
      public void run() {
        if(GeneratorPillar.countMaterial(guildGenerator) <= 0 && !guildGenerator.getGeneratorType().equals(GuildGeneratorType.AIR)){
          API.getInstance().getGuildService().findByValueOptional(guildGenerator.getGuildName()).ifPresent(guild -> {
            guild.findGuildGeneratorByType(guildGenerator.getGeneratorType()).ifPresent(guildGeneratorFind -> {
              guildGeneratorFind.setActiveTime(0);
              guildGeneratorFind.setBlocks(0);
              guildGeneratorFind.setSkippedBlocks(0);
              guildGeneratorFind.setLocationCornerFirst(new LocationSerializer("world",  0, 0, 0, 0));
              guildGeneratorFind.setLocationCornerSecond(new LocationSerializer("world",  0, 0, 0, 0));
            });
            guild.sendMessage("&5&lGILDIA &8>> &fGeneratorowi &dskończyły &fsię materiały i potrzebuje &dponownego &furuchomienia.");
            guild.synchronize(SynchronizeType.UPDATE);
          });
          this.cancel();
          return;
        }
        if (area <= index.get()) {
          API.getInstance().getGuildService().findByValueOptional(guildGenerator.getGuildName()).ifPresent(guild -> {
            guild.findGuildGeneratorByType(guildGenerator.getGeneratorType()).ifPresent(guildGeneratorFind -> {
              guildGeneratorFind.setBlocks(0);
              guildGeneratorFind.setSkippedBlocks(0);
              guildGeneratorFind.setLocationCornerFirst(new LocationSerializer("world",  0, 0, 0, 0));
              guildGeneratorFind.setLocationCornerSecond(new LocationSerializer("world",  0, 0, 0, 0));
            });
            guild.sendMessage("&5&lGILDIA &8>> &fGenerator &dzakończył &fswoją prace.");
            guild.synchronize(SynchronizeType.UPDATE);
          });
          this.cancel();
          return;
        }
        GeneratorPillar pillar = region.pillar(index.getAndIncrement());
        new GeneratorPillarFiller(guildGenerator, pillar, region.getMaxY() + 1,region.getMinY(), material).fill();
        count++;
      }
    }.runTaskTimer(BukkitAPI.getInstance(), 0L, 20L);
  }

  @Override
  public void completeNow() {
    for (GeneratorPillar pillar : this.region.pillars()) {
      new GeneratorPillarFiller(guildGenerator, pillar, region.getMaxY() + 1,region.getMinY(), material).fill();
    }
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}