package net.moremc.api.data.drop.chest;

import net.moremc.api.configuration.impl.ConfigurationImpl;

public class DropChestDataConfiguration extends ConfigurationImpl<DropChestData> {


    public DropChestDataConfiguration() {
        super("dropChest", DropChestData.class);
    }
}
