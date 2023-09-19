package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.other.QuestInventory;

public class QuestCommand
{
    private final ToolsPlugin toolsPlugin;
    private final QuestInventory questInventory = new QuestInventory();

    public QuestCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
    }

    @Command(value = {"quest", "zadania", "zadanie"}, description = "Otwiera GUI z zadaniami")
    public void handle(@Sender Player player) {
        this.questInventory.show(player);
    }
}
