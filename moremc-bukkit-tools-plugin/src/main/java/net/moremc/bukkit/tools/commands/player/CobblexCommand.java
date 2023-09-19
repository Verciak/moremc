package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.bukkit.tools.service.CustomItemService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.RandomHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class CobblexCommand
{
    private final ToolsPlugin plugin;
    private final CustomItemService service;

    public CobblexCommand(ToolsPlugin plugin, CustomItemService service) {
        this.plugin = plugin;
        this.service = service;
    }

    @Command(value = { "cobblex", "cx" }, description = "Tworzy cobblex'a")
    public void handle(@Sender Player player) {
        if (!player.getInventory().containsAtLeast(new ItemStack(Material.COBBLESTONE), 576)) {
            throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz wystarczająco dużo przedmiotów aby wykonać ten przedmiot"));
        }
        ItemStack cobblexItem = this.service.find("cobblex");
        if(cobblexItem == null) {
            throw new BladeExitMessage(MessageHelper.colored("&cWystąpił błąd nie znaleziono itemu 'Cobblex' !"));
        }
        int cobblexSize = RandomHelper.getInt(1, 3);

        cobblexItem.setAmount(cobblexSize);
        player.getInventory().removeItem(new ItemStack(Material.COBBLESTONE, 576));
        player.getInventory().addItem(cobblexItem);

        player.sendMessage(MessageHelper.colored("&fPomyślnie utworzono &dCobblex'a &fw ilości &d" + cobblexSize));
    }
}
