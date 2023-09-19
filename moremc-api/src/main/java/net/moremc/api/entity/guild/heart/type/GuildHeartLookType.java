package net.moremc.api.entity.guild.heart.type;

import java.io.Serializable;

public enum GuildHeartLookType implements Serializable {


    ENDER_STONE("ender_stone"),
    OBSIDIAN("obsidian"),
    NETHER("nether"),
    SAND("sand"),
    PUMPKIN("pumpkin");


    private final String schematicName;

    GuildHeartLookType(String schematicName){
        this.schematicName = schematicName;
    }
    public String getSchematicName() {
        return schematicName;
    }
}
