package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.kit.Kit;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.KitService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.bukkit.tools.inventories.admin.AdminKitConfigurationGlassInventory;
import net.moremc.bukkit.tools.inventories.admin.AdminKitConfigurationItemsInventory;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminKitConfigurationCommand {


    private final KitService kitService = API.getInstance().getKitService();
    private final UserService userService = API.getInstance().getUserService();

    public AdminKitConfigurationCommand(ToolsPlugin toolsPlugin) {

    }

    @Command(value = { "akits create", "adminkit create" }, description = "Konfiguruje zestawy serwerowe")
    public void handleCreate(@Sender Player player, @Name("name") String kitName, @Name("delay") int delayTime, @Name("inventorySlot") int inventorySlot,
                             @Combined String inventoryName){

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.DEVELOPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (this.kitService.findByValueOptional(kitName).isPresent()) {
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cZestaw o takiej nazwie już istnieje."));
                return;
            }
            Kit kit = new Kit(kitName, inventoryName, delayTime, inventorySlot);
            player.sendMessage(MessageHelper.translateText("&aPomyślnie utworzono zestaw&8: &2" + kit.getName()));
        });
    }
    @Command(value = { "akits", "adminkit" }, description = "Konfiguruje zestawy serwerowe")
    public void handleEdit(@Sender Player player, @Combined String argument) {
        String[] args = argument.split(" ");

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.DEVELOPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            switch (args[0]) {
                case "edit": {

                    if (args.length < 3) {
                        player.sendMessage(MessageHelper.translateText("&7Poprawne użycie: &d/akit edit <name> <items/glass/name/slot/permission/material/delay>"));
                        return;
                    }

                    String kitName = args[1];

                    this.kitService.findByValueOptional(kitName).ifPresent(kit -> {
                        switch (args[2]) {
                            case "items": {
                                AdminKitConfigurationItemsInventory itemsInventory = new AdminKitConfigurationItemsInventory(kit);
                                itemsInventory.show(player);
                                break;
                            }
                            case "glass": {
                                AdminKitConfigurationGlassInventory glassInventory = new AdminKitConfigurationGlassInventory(kit);
                                glassInventory.show(player);
                                break;
                            }
                            case "name": {
                                String inventoryName = StringUtils.join(args, " ", 3, args.length);
                                kit.setInventoryName(inventoryName);
                                kit.synchronize(SynchronizeType.UPDATE);

                                player.sendMessage(MessageHelper.translateText("&aPomyślnie skonfigurwano zestaw&8(&2" + kit.getName() + "&8)"));
                                break;
                            }
                            case "permission": {
                                String groupName = args[3];
                                if (UserGroupType.groupExists(groupName)) {
                                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodana grupa nie istnieje."));
                                    return;
                                }
                                kit.setPermissionType(UserGroupType.valueOf(groupName));
                                kit.synchronize(SynchronizeType.UPDATE);
                                player.sendMessage(MessageHelper.translateText("&aPomyślnie skonfigurwano zestaw&8(&2" + kit.getName() + "&8)"));
                                break;
                            }
                            case "slot": {
                                try {
                                    Integer.parseInt(args[3]);
                                } catch (Exception e) {
                                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodaj poprawną liczbę."));
                                    return;
                                }
                                kit.setInventorySlot(Integer.parseInt(args[3]));
                                kit.synchronize(SynchronizeType.UPDATE);
                                player.sendMessage(MessageHelper.translateText("&aPomyślnie skonfigurwano zestaw&8(&2" + kit.getName() + "&8)"));
                                break;
                            }
                            case "delay": {
                                try {
                                    Integer.parseInt(args[3]);
                                } catch (Exception e) {
                                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodaj poprawną liczbę."));
                                    return;
                                }
                                kit.setDelayTime(Integer.parseInt(args[3]));
                                kit.synchronize(SynchronizeType.UPDATE);
                                player.sendMessage(MessageHelper.translateText("&aPomyślnie skonfigurwano zestaw&8(&2" + kit.getName() + "&8)"));
                                break;
                            }
                            case "material": {
                                try {
                                    Material.valueOf(args[3]);
                                } catch (Exception e) {
                                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cTaki przedmiot nie istnieje."));
                                    return;
                                }
                                kit.setMaterialInventory(args[3]);
                                kit.synchronize(SynchronizeType.UPDATE);
                                player.sendMessage(MessageHelper.translateText("&aPomyślnie skonfigurwano zestaw&8(&2" + kit.getName() + "&8)"));
                                break;
                            }
                        }
                    });
                    break;
                }
            }
        });
    }
}
