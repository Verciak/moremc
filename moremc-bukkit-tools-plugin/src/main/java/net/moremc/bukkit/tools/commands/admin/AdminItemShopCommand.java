package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.player.PlayerItemShopPacket;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.data.ItemShop;
import net.moremc.bukkit.tools.service.CustomItemService;
import net.moremc.bukkit.tools.service.ItemShopService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class AdminItemShopCommand
{
    private final ToolsPlugin plugin;
    private final API api;

    private final UserService userService;
    private final ItemShopService itemShopService;
    private final CustomItemService customItemService;

    public AdminItemShopCommand(ToolsPlugin plugin, ItemShopService itemShopService, CustomItemService customItemService) {
        this.plugin = plugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
        this.itemShopService = itemShopService;
        this.customItemService = customItemService;
    }
    @Command(value = { "adminitemshop list", "adminis give", "ais list"}, description = "Komenda sprawdzająca dostępne usługi")
    public void handle(@Sender Player player) {
        itemShopService.getItemShops().forEach(itemShop -> {
            player.sendMessage(MessageHelper.colored(" &8» &d" + itemShop.getName() + " &f, wykonywna komenda: &d" + itemShop.getCommand()));
        });
    }
    @Command(value = { "adminitemshop give", "adminis give", "ais give"}, description = "Komenda do nadawania przedmiotów w itemshopie")
    public void handle(@Sender Player player, @Name("name") final String name, @Name("service") final String service) {

        this.userService.findByValueOptional(player.getName()).ifPresent(auser -> {
            if (!UserGroupType.hasPermission(UserGroupType.DEVELOPER, auser)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            Optional<User> user = api.getUserService().findByValueOptional(name);
            if (!user.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
            }
            Optional<ItemShop> itemShop = itemShopService.find(service);
            if(!itemShop.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie znaleziono takiej usługi!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new PlayerItemShopPacket(user.get().getNickName(), itemShop.get().getCommand().replace("{PLAYER}", user.get().getNickName())));
        });
    }
    @Command(value = { "ais addDatabase"}, description = "Dodaje item do GUI z itemshopem")
    public void handleAdd(@Sender Player player, @Name("name") final String name, @Name("item") final String item, final @Name("amount") int amount) {
        this.userService.findByValueOptional(player.getName()).ifPresent(auser -> {
            if (!UserGroupType.hasPermission(UserGroupType.DEVELOPER, auser)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            Optional<User> user = api.getUserService().findByValueOptional(name);
            if (!user.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
            }
            ItemStack toAddItem = customItemService.find(item);
            if(toAddItem == null) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie znaleziono takiego itemu!"));
            }
            toAddItem.setAmount(amount);

            if(user.get().getItemShopItems().equalsIgnoreCase("null")) {
                //TODO
            } else {
                //TODO
            }
        });
    }
}
