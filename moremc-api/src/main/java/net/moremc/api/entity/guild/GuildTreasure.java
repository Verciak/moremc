package net.moremc.api.entity.guild;

import java.io.Serializable;

public class GuildTreasure implements Serializable {

    private boolean open = false;
    private String serializedItems = "null";

    public String getSerializedItems() {
        return serializedItems;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setSerializedItems(String serializedItems) {
        this.serializedItems = serializedItems;
    }
}
