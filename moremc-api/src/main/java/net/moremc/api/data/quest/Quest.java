package net.moremc.api.data.quest;

import net.moremc.api.entity.user.type.UserQuestType;

import java.io.Serializable;

public class Quest implements Serializable {

    public int id;
    public int inventorySlot;
    public int amountRequired;
    public String description;
    public QuestRewardType rewardType;
    public int amountReward;
    private int questMinuteLeft;
    public String itemParserReward;
    public int progressLine;
    public int progress;
    public UserQuestType questType;
    public String materialSkullUrl;


    public int getQuestMinuteLeft() {
        return questMinuteLeft;
    }

    public String getDescription() {
        return description;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getItemParserReward() {
        return itemParserReward;
    }

    public int getProgressLine() {
        return progressLine;
    }

    public int getProgress() {
        return progress;
    }

    public int getAmountReward() {
        return amountReward;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

    public String getMaterialSkullUrl() {
        return materialSkullUrl;
    }

    public int getId() {
        return id;
    }

    public QuestRewardType getRewardType() {
        return rewardType;
    }

    public UserQuestType getQuestType() {
        return questType;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", inventorySlot=" + inventorySlot +
                ", amountRequired=" + amountRequired +
                ", description='" + description + '\'' +
                ", rewardType=" + rewardType +
                ", amountReward=" + amountReward +
                ", questMinuteLeft=" + questMinuteLeft +
                ", itemParserReward='" + itemParserReward + '\'' +
                ", progressLine=" + progressLine +
                ", progress=" + progress +
                ", questType=" + questType +
                ", materialSkullUrl='" + materialSkullUrl + '\'' +
                '}';
    }
}
