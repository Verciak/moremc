package net.moremc.bukkit.tools.commands.player;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.moremc.bukkit.tools.helper.BackPackHelper;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.service.entity.BackPackService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

import java.util.UUID;


public class BackPackDescriptionCommand
{
    private ToolsPlugin plugin;

    private BackPackService service;

    public BackPackDescriptionCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.service = API.getInstance().getBackPackService();
    }

    @Command(value = { "opis", "description" }, description = "Zmienia opis plecaka")
    public void handle(@Sender Player player, @Name("description") @Combined String description) {
        if(description.trim().isEmpty()) {
            throw new BladeExitMessage(MessageHelper.colored("&cOpis plecaka nie może być pusty"));
        }
        if (description.length() > 16) {
            throw new BladeExitMessage(MessageHelper.colored("&cOpis może zawierać maksymalnie &716 &cznaków!"));
        }
        ItemStack item = player.getItemInHand();
        NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
        if(!tag.hasKey("backpack-uuid")) {
            player.sendMessage(MessageHelper.colored("&cItem który trzymasz w ręce nie jest plecakiem"));
            return;
        }
        UUID backPackUUID = UUID.fromString(tag.getString("backpack-uuid"));
        service.findByValueOptional(backPackUUID).ifPresent(backPack -> {
            if (!backPack.getOwnerUUID().equals(player.getUniqueId())) {
                player.sendMessage(MessageHelper.colored("&cNie jesteś właścicielem tego plecaka"));
                return;
            }
            backPack.setDescription(description);

            player.getInventory().setItem(player.getInventory().first(item), BackPackHelper.getBackPackItem(backPack, backPack.getType()));
            player.sendMessage(MessageHelper.colored("&fPomyślnie zmieniono opis plecka na &d" + description + "&f!"));
        });
    }
}
