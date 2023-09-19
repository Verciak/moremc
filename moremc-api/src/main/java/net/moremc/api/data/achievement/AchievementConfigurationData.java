package net.moremc.api.data.achievement;

import net.moremc.api.configuration.impl.ConfigurationImpl;

public class AchievementConfigurationData extends ConfigurationImpl<AchievementData> {

    public AchievementConfigurationData() {
        super("achievement", AchievementData.class);
    }
}
