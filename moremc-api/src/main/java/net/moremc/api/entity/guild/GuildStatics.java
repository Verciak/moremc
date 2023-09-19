package net.moremc.api.entity.guild;

import net.moremc.api.helper.type.TimeType;

import java.io.Serializable;

public class GuildStatics implements Serializable {

    private int points;
    private int kills;
    private int deaths;
    private int lifes;
    private int lifeHp;
    private long protection;

    public GuildStatics(){
        this.points = 1000;
        this.kills = 0;
        this.deaths = 0;
        this.lifes = 3;
        this.lifeHp = 30;
        this.protection = System.currentTimeMillis() + TimeType.DAY.getTime(1);
    }

    public long getProtection() {
        return protection;
    }

    public void setProtection(long protection) {
        this.protection = protection;
    }
    public boolean hasProtection(){
        return this.protection <= System.currentTimeMillis();
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void setLifeHp(int lifeHp) {
        this.lifeHp = lifeHp;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getPoints() {
        return points;
    }

    public int getLifes() {
        return lifes;
    }

    public int getLifeHp() {
        return lifeHp;
    }
}
