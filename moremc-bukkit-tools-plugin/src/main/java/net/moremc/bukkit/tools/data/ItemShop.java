package net.moremc.bukkit.tools.data;

import java.io.Serializable;

public class ItemShop implements Serializable
{
    private String name;
    private String command;

    public ItemShop(String name, String command) {
        this.name = name;
        this.command = command;
    }
    public String getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }
}
