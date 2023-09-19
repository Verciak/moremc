package net.moremc.api.entity.guild.generator;

import net.moremc.api.entity.guild.generator.magazine.GuildGeneratorMagazine;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.serializer.LocationSerializer;

import java.io.Serializable;

public class GuildGenerator implements Serializable {


    private final String guildName;
    private final GuildGeneratorType generatorType;

    private final String materialSkullItemOwnerUrl;

    private final GuildGeneratorMagazine magazine;

    private LocationSerializer locationCornerFirst;
    private LocationSerializer locationCornerSecond;


    private String clickedActiveNickname;
    private long activeTime;
    private int skippedBlocks = 0;
    private int blocks = 0;
    private int successBlocks = 0;


    public GuildGenerator(String guildName, GuildGeneratorType generatorType, String materialSkullItemOwnerUrl){
        this.guildName = guildName;
        this.generatorType = generatorType;
        this.magazine = new GuildGeneratorMagazine();
        this.materialSkullItemOwnerUrl = materialSkullItemOwnerUrl;
        this.locationCornerFirst = new LocationSerializer("world", 0, 0, 0, 0);
        this.locationCornerSecond = new LocationSerializer("world", 0, 0, 0, 0);
        this.clickedActiveNickname = "null";
        this.activeTime = 0L;
    }

    public String getGuildName() {
        return guildName;
    }

    public int getSkippedBlocks() {
        return skippedBlocks;
    }

    public void setSkippedBlocks(int skippedBlocks) {
        this.skippedBlocks = skippedBlocks;
    }

    public String getClickedActiveNickname() {
        return clickedActiveNickname;
    }

    public String getMaterialSkullItemOwnerUrl() {
        return materialSkullItemOwnerUrl;
    }

    public boolean hasSetLocationFirst(){
        return this.locationCornerFirst.getX() != 0 && this.locationCornerFirst.getY() != 0 && this.locationCornerFirst.getZ() != 0;
    }
    public boolean hasSetLocationSecond(){
        return this.locationCornerSecond.getX() != 0 && this.locationCornerSecond.getY() != 0 && this.locationCornerSecond.getZ() != 0;
    }

    public LocationSerializer getLocationCornerSecond() {
        return locationCornerSecond;
    }

    public LocationSerializer getLocationCornerFirst() {
        return locationCornerFirst;
    }

    public long getActiveTime() {
        return activeTime;
    }
    public boolean isActive(){
        return this.activeTime <= System.currentTimeMillis();
    }

    public GuildGeneratorType getGeneratorType() {
        return generatorType;
    }

    public GuildGeneratorMagazine getMagazine() {
        return magazine;
    }

    public void setLocationCornerSecond(LocationSerializer locationCornerSecond) {
        this.locationCornerSecond = locationCornerSecond;
    }

    public void setClickedActiveNickname(String clickedActiveNickname) {
        this.clickedActiveNickname = clickedActiveNickname;
    }

    public void setLocationCornerFirst(LocationSerializer locationCornerFirst) {
        this.locationCornerFirst = locationCornerFirst;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getAllCountBlocks() {
        return this.blocks;
    }

    public int getSuccessBlocks() {
        return this.successBlocks;
    }

    public void setSuccessBlocks(int successBlocks) {
        this.successBlocks = successBlocks;
    }
}
