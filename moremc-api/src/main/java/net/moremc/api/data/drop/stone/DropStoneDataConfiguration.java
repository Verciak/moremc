package net.moremc.api.data.drop.stone;


import net.moremc.api.configuration.impl.ConfigurationImpl;

public class DropStoneDataConfiguration extends ConfigurationImpl<DropStoneData> {

    public DropStoneDataConfiguration() {
        super("dropStone", DropStoneData.class);
    }
}
