package net.moremc.api.entity.guild.generator.magazine;

import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.nats.packet.type.SynchronizeType;

import java.io.Serializable;

public class GuildGeneratorMagazine implements Serializable {

    private String serializedInventory;
    private boolean open;

    public GuildGeneratorMagazine(){
        this.serializedInventory = "null";
        this.open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public String getSerializedInventory() {
        return serializedInventory;
    }

    public void setOpen(GuildImpl guild, boolean open) {
        this.open = open;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public void setSerializedInventory(GuildImpl guild, String serializedInventory) {
        this.serializedInventory = serializedInventory;
        guild.synchronize(SynchronizeType.UPDATE);
    }
    public void setSerializedInventory(String serializedInventory) {
        this.serializedInventory = serializedInventory;
    }
}
