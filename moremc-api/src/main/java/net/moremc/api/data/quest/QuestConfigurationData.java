package net.moremc.api.data.quest;

import net.moremc.api.configuration.impl.ConfigurationImpl;

public class QuestConfigurationData extends ConfigurationImpl<QuestData> {

    public QuestConfigurationData() {
        super("quests", QuestData.class);
    }
}
