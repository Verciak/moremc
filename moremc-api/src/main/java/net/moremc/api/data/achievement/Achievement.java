package net.moremc.api.data.achievement;


import net.moremc.api.data.achievement.type.AchievementRewardType;
import net.moremc.api.entity.user.type.UserAchievementType;

import java.io.Serializable;

public class Achievement implements Serializable {

    public int id;
    public int inventorySlot;
    public int amountRequired;
    public AchievementRewardType rewardType;
    public int amountReward;
    public String itemParserReward;
    public int progressLine;
    public int progress;
    public UserAchievementType achievementType;
    public String materialSkullUrl;

    public String getItemParserReward() {
        return itemParserReward;
    }

    public int getProgress() {
        return progress;
    }

    public int getProgressLine() {
        return progressLine;
    }

    public UserAchievementType getAchievementType() {
        return achievementType;
    }

    public int getId() {
        return id;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getMaterialSkullUrl() {
        return materialSkullUrl;
    }

    public AchievementRewardType getRewardType() {
        return rewardType;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

    public int getAmountReward() {
        return amountReward;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", inventorySlot=" + inventorySlot +
                ", amountRequired=" + amountRequired +
                ", rewardType=" + rewardType +
                ", amountReward=" + amountReward +
                ", itemParserReward='" + itemParserReward + '\'' +
                ", progressLine=" + progressLine +
                ", progress=" + progress +
                ", achievementType=" + achievementType +
                ", materialSkullUrl='" + materialSkullUrl + '\'' +
                '}';
    }
}