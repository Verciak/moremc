package net.moremc.bukkit.api.generator.player;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import net.moremc.bukkit.api.generator.GeneratorRegion;

import java.util.Map;
import java.util.UUID;

public class GeneratorPlayer  {

  private final Map<String, Long> lastKitUseTimeStamp = Maps.newHashMap();

  private UUID uuid;
  private String name;

  public GeneratorPlayer(){

  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public Location getFirstCorner() {
    return firstCorner;
  }

  public Location getSecondCorner() {
    return secondCorner;
  }

  private Location firstCorner;
  private Location secondCorner;

  public void setFirstCorner(Location location) {
    this.firstCorner = location;
  }

  public void setSecondCorner(Location location) {
    this.secondCorner = location;
  }

  public boolean isFarmerRegionReady() {
    return this.firstCorner != null && this.secondCorner != null;
  }

  public GeneratorRegion farmerRegion() {
    return GeneratorRegion.fromCorners(this.firstCorner, this.secondCorner);
  }

  public int farmerRegionPrice() {
    return this.farmerRegion().price();
  }

  private void applyCorner(GeneratorRegion region, Location corner) {
    region.setX(corner.getBlockX());
    region.setY(corner.getBlockY());
    region.setZ(corner.getBlockZ());
  }
}