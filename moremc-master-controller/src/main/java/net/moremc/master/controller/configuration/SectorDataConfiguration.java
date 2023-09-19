package net.moremc.master.controller.configuration;

import net.moremc.api.configuration.impl.ConfigurationImpl;
import net.moremc.api.data.SectorData;

public class SectorDataConfiguration extends ConfigurationImpl<SectorData[]>
{

    public SectorDataConfiguration() {
        super("sectors", SectorData[].class);
    }
}
