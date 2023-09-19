package net.moremc.api.data.quest;

import java.io.Serializable;
import java.util.Arrays;

public class QuestData implements Serializable {

    public String inventoryName;
    public String[] inventoryLore;
    public Quest[] quests;


    public String getInventoryName() {
        return inventoryName;
    }

    public String[] getInventoryLore() {
        return inventoryLore;
    }

    public Quest[] getQuests() {
        return quests;
    }
    public Quest findQuestById(int id){
        return Arrays.stream(this.quests)
                .filter(quest -> quest.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public Quest findQuestBySlot(int id){
        return Arrays.stream(this.quests)
                .filter(quest -> quest.getInventorySlot() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "QuestData{" +
                "inventoryName='" + inventoryName + '\'' +
                ", inventoryLore=" + Arrays.toString(inventoryLore) +
                ", quests=" + Arrays.toString(quests) +
                '}';
    }
}
