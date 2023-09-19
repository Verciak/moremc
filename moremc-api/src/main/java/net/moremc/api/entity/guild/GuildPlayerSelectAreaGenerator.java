package net.moremc.api.entity.guild;

import net.moremc.api.serializer.LocationSerializer;

public class GuildPlayerSelectAreaGenerator {


    private final String nickName;
    private final LocationSerializer guildLocation;
    private LocationSerializer locationCornerFirst;
    private LocationSerializer locationCornerSecond;

    public GuildPlayerSelectAreaGenerator(String nickName, LocationSerializer guildLocation) {
        this.nickName = nickName;
        this.guildLocation = guildLocation;
        this.locationCornerFirst = new LocationSerializer("world", 0, 0, 0, 0);
        this.locationCornerSecond = new LocationSerializer("world", 0, 0, 0, 0);
    }
    public boolean hasSetLocationFirst(){
        return this.locationCornerFirst.getX() != 0 && this.locationCornerFirst.getY() != 0 && this.locationCornerFirst.getZ() != 0;
    }
    public boolean hasSetLocationSecond(){
        return this.locationCornerSecond.getX() != 0 && this.locationCornerSecond.getY() != 0 && this.locationCornerSecond.getZ() != 0;
    }

    public String getNickName() {
        return nickName;
    }

    public LocationSerializer getGuildLocation() {
        return guildLocation;
    }

    public LocationSerializer getLocationCornerFirst() {
        return locationCornerFirst;
    }

    public LocationSerializer getLocationCornerSecond() {
        return locationCornerSecond;
    }

    public void setLocationCornerFirst(LocationSerializer locationCornerFirst) {
        this.locationCornerFirst = locationCornerFirst;
    }

    public void setLocationCornerSecond(LocationSerializer locationCornerSecond) {
        this.locationCornerSecond = locationCornerSecond;
    }

}
