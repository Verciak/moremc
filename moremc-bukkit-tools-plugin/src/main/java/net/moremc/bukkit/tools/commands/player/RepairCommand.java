package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Permission;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class RepairCommand
{
    private final ToolsPlugin plugin;

    public RepairCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
    }
    @Permission("moremc.command.repair")
    @Command(value = { "repair", "napraw" }, description = "Naprawia przedmiot w ręce")
    public void handle(@Sender Player player) {
        ItemStack itemInHand = player.getItemInHand();
        if (!canRepair(itemInHand)) {
            throw new BladeExitMessage(MessageHelper.colored("&cTego przedmiotu nie mozesz naprawic!"));
        }
        if(itemInHand.getDurability() == 1) {
            throw new BladeExitMessage(MessageHelper.colored("&cTen przedmiot jest naprawiony"));
        }
        itemInHand.setDurability((short) 0);
        player.playSound(player.getLocation(), Sound.ANVIL_USE, 2f, 2f);
        player.sendMessage(MessageHelper.colored("&fPomyślnie naprawiono przedmiot &d" + itemInHand.getType() + " &f!"));
    }
    @Permission("moremc.command.repairall")
    @Command(value = {"repairall", "naprawwszystko"}, description = "Naprawia wszyskie przedmioty w EQ")
    public void handleAll(@Sender Player player) {
        PlayerInventory inventory = player.getInventory();
        for (ItemStack content : inventory.getContents()) {
            if (content != null && content.getType() != null && content.getType().getMaxDurability() > 0) {
                content.setDurability((short) 0);
            }
        }
        for (ItemStack content : inventory.getArmorContents()) {
            if (content != null && content.getType().getMaxDurability() > 0) {
                content.setDurability((short) 0);
            }
        }
        player.updateInventory();
        player.playSound(player.getLocation(), Sound.ANVIL_USE, 2f, 2f);
        player.sendMessage(MessageHelper.colored("&fPomyślnie naprawiono &dwszystkie &fprzedmioty !"));
    }
    public static boolean canRepair(ItemStack item) {
        if (item == null) {
            return false;
        }
        boolean can = false;
        if (item.getType().name().contains("SWORD")) {
            can = true;
        }
        if (item.getType().name().contains("SPADE")) {
            can = true;
        }
        if (item.getType().name().contains("PICKAXE")) {
            can = true;
        }
        if (item.getType().name().contains("AXE")) {
            can = true;
        }
        if (item.getType().name().contains("HOE")) {
            can = true;
        }
        if (item.getType().name().contains("HELMET")) {
            can = true;
        }
        if (item.getType().name().contains("CHESTPLATE")) {
            can = true;
        }
        if (item.getType().name().contains("LEGGINGS")) {
            can = true;
        }
        if (item.getType().name().contains("BOOTS")) {
            can = true;
        }
        if (item.getType() == Material.SHEARS) {
            can = true;
        }
        if (item.getType() == Material.BOW) {
            can = true;
        }
        return can;
    }
}
