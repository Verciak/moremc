package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.bukkit.tools.service.CustomItemService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.ServerService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class AdminItemsCommand
{
    private final ToolsPlugin plugin;

    private final CustomItemService service;
    private final UserService userService;

    public AdminItemsCommand(ToolsPlugin plugin, CustomItemService service) {
        this.plugin = plugin;
        this.service = service;
        this.userService = API.getInstance().getUserService();
    }
    @Command(value = { "adminitems", "customitems" }, description = "Otwiera GUI z wszystkimi customowymi itemy z serwera")
    public void handle(@Sender Player player) {

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.MODERATOR, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            Inventory inventory = Bukkit.createInventory(player, 18, MessageHelper.colored("&fAdminItems"));
            service.getItems().forEach((name, item) -> {
                inventory.addItem(item);
            });
            player.openInventory(inventory);
        });
    }
}
