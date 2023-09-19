package net.moremc.api.data.guild.dues;

import net.moremc.api.configuration.impl.ConfigurationImpl;

public class DuesGuildDataConfiguration extends ConfigurationImpl<DuesGuildData> {

    public DuesGuildDataConfiguration() {
        super("dues", DuesGuildData.class);
    }
}
