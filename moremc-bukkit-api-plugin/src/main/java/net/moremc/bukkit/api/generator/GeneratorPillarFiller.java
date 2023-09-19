package net.moremc.bukkit.api.generator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import net.moremc.api.API;
import net.moremc.api.entity.guild.generator.GuildGenerator;

public class GeneratorPillarFiller implements Filler {

    private final GuildGenerator guildGenerator;
    private final GeneratorPillar pillar;
    private final int maxY;
    private final int secondY;
    private final Material material;

    public GeneratorPillarFiller(GuildGenerator guildGenerator, GeneratorPillar pillar, int maxY, int secondY, Material material) {
        this.guildGenerator = guildGenerator;
        this.maxY = maxY;
        this.pillar = pillar;
        this.secondY = secondY;
        this.material = material;
    }

    @Override
    public void fill() {
        API.getInstance().getGuildService().findByValueOptional(guildGenerator.getGuildName()).ifPresent(guild -> {
            guild.findGuildGeneratorByType(this.guildGenerator.getGeneratorType()).ifPresent(guildGeneratorFind -> {
                this.pillar.fill(guildGeneratorFind, Bukkit.getWorld("world"), this.material, this.secondY, this.maxY);
            });
        });
    }

    @Override
    public void completeNow() {
        this.fill();
    }
}

