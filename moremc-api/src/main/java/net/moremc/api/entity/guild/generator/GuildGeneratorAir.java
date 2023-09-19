package net.moremc.api.entity.guild.generator;

import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;

import java.io.Serializable;

public class GuildGeneratorAir extends GuildGenerator implements Serializable {

    public GuildGeneratorAir(String guildName) {
        super(guildName, GuildGeneratorType.AIR, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzllM2JkYTU0NzQ1YTdjZGU0YzIzMzU3MjhlMjc1N2YzZGFmMmJmMzY3YzJlZmQ4ZTQwYzUyMDczZjU5NzAzNSJ9fX0=");
    }
}
