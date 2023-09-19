package net.moremc.api.entity.user;

import net.moremc.api.serializer.SerializablePotionEffect;

import java.io.Serializable;

public class UserSector implements Serializable {

    private boolean canInteract = true;
    private String actualSectorName;
    private String locationWorldName;
    private double locationX;
    private double locationY;
    private double locationZ;
    private float locationYaw;
    private float locationPitch;
    private double health;
    private double foodLevel;
    private String serializedInventoryContent;
    private String serializedInventoryEnderchestContent;
    private String serializedInventoryArmorContent;
    private SerializablePotionEffect[] serializablePotionEffects;
    private int inventoryHeldItemSlot;
    private String gameModeName;
    private boolean flying;
    private long sectorChange = 0;

    public SerializablePotionEffect[] getSerializablePotionEffects() {
        return serializablePotionEffects;
    }

    public void setSerializablePotionEffects(SerializablePotionEffect[] serializablePotionEffects) {
        this.serializablePotionEffects = serializablePotionEffects;
    }

    public void setSerializedInventoryEnderchestContent(String serializedInventoryEnderchestContent) {
        this.serializedInventoryEnderchestContent = serializedInventoryEnderchestContent;
    }

    public String getSerializedInventoryEnderchestContent() {
        return serializedInventoryEnderchestContent;
    }

    public long getSectorChange() {
        return sectorChange;
    }

    public void setSectorChange(long sectorChange) {
        this.sectorChange = sectorChange;
    }

    public boolean hasSectorChangeDelay(){
        return this.sectorChange <= System.currentTimeMillis();
    }

    public float getLocationPitch() {
        return locationPitch;
    }

    public float getLocationYaw() {
        return locationYaw;
    }

    public boolean isCanInteract() {
        return canInteract;
    }

    public double getHealth() {
        return health;
    }
    public double getFoodLevel() {
        return foodLevel;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public double getLocationZ() {
        return locationZ;
    }

    public int getInventoryHeldItemSlot() {
        return inventoryHeldItemSlot;
    }

    public String getActualSectorName() {
        return actualSectorName;
    }

    public String getGameModeName() {
        return gameModeName;
    }

    public String getLocationWorldName() {
        return locationWorldName;
    }

    public String getSerializedInventoryArmorContent() {
        return serializedInventoryArmorContent;
    }

    public String getSerializedInventoryContent() {
        return serializedInventoryContent;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;
    }

    public void setActualSectorName(String actualSectorName) {
        this.actualSectorName = actualSectorName;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public void setFoodLevel(double foodLevel) {
        this.foodLevel = foodLevel;
    }

    public void setGameModeName(String gameModeName) {
        this.gameModeName = gameModeName;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setInventoryHeldItemSlot(int inventoryHeldItemSlot) {
        this.inventoryHeldItemSlot = inventoryHeldItemSlot;
    }

    public void setLocationWorldName(String locationWorldName) {
        this.locationWorldName = locationWorldName;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public void setLocationZ(double locationZ) {
        this.locationZ = locationZ;
    }

    public void setSerializedInventoryArmorContent(String serializedInventoryArmorContent) {
        this.serializedInventoryArmorContent = serializedInventoryArmorContent;
    }

    public void setSerializedInventoryContent(String serializedInventoryContent) {
        this.serializedInventoryContent = serializedInventoryContent;
    }

    public void setLocationPitch(float locationPitch) {
        this.locationPitch = locationPitch;
    }

    public void setLocationYaw(float locationYaw) {
        this.locationYaw = locationYaw;
    }


    @Override
    public String toString() {
        return "UserSector{" +
                "canInteract=" + canInteract +
                ", actualSectorName='" + actualSectorName + '\'' +
                ", locationWorldName='" + locationWorldName + '\'' +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", locationZ=" + locationZ +
                ", health=" + health +
                ", foodLevel=" + foodLevel +
                ", serializedInventoryContent='" + serializedInventoryContent + '\'' +
                ", serializedInventoryArmorContent='" + serializedInventoryArmorContent + '\'' +
                ", inventoryHeldItemSlot=" + inventoryHeldItemSlot +
                ", gameModeName='" + gameModeName + '\'' +
                ", flying=" + flying +
                '}';
    }
}
