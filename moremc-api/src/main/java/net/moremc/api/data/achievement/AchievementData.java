package net.moremc.api.data.achievement;

import net.moremc.api.entity.user.type.UserAchievementType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

public class AchievementData implements Serializable {

    public String inventoryName;
    public String[] inventoryLore;
    public Achievement[] achievements;


    public Achievement[] getAchievements() {
        return achievements;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public String[] getInventoryLore() {
        return inventoryLore;
    }


    public Optional<Achievement> findAchievement(UserAchievementType achievementType, int slot){
        return Arrays.stream(this.achievements)
                .filter(achievement -> achievement.getAchievementType().equals(achievementType))
                .filter(achievement -> achievement.getInventorySlot() == slot)
                .findFirst();
    }

    @Override
    public String toString() {
        return "AchievementData{" +
                "inventoryName='" + inventoryName + '\'' +
                ", inventoryLore=" + Arrays.toString(inventoryLore) +
                ", achievements=" + Arrays.toString(achievements) +
                '}';
    }
}