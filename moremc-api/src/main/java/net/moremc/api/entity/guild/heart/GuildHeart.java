package net.moremc.api.entity.guild.heart;

import net.moremc.api.entity.guild.heart.type.GuildHeartEggType;
import net.moremc.api.entity.guild.heart.type.GuildHeartLookType;
import net.moremc.api.entity.guild.heart.type.GuildHeartParticleType;

import java.io.Serializable;

public class GuildHeart implements Serializable {


    private GuildHeartEggType eggType;
    private GuildHeartLookType lookType;
    private GuildHeartParticleType particleType;

    public GuildHeart(){
        this.eggType = GuildHeartEggType.ORB;
        this.lookType = GuildHeartLookType.OBSIDIAN;
        this.particleType = GuildHeartParticleType.EMERALD;
    }

    public GuildHeartEggType getEggType() {
        return eggType;
    }
    public GuildHeartLookType getLookType() {
        return lookType;
    }
    public GuildHeartParticleType getParticleType() {
        return particleType;
    }
    public void setEggType(GuildHeartEggType eggType) {
        this.eggType = eggType;
    }
    public void setLookType(GuildHeartLookType lookType) {
        this.lookType = lookType;
    }
    public void setParticleType(GuildHeartParticleType particleType) {
        this.particleType = particleType;
    }
}
