package net.moremc.api.data.guild;

import net.moremc.api.configuration.impl.ConfigurationImpl;

public class GuildPermissionDataConfiguration extends ConfigurationImpl<GuildPermissionData> {

    public GuildPermissionDataConfiguration() {
        super("guildPermission", GuildPermissionData.class);
    }
}
