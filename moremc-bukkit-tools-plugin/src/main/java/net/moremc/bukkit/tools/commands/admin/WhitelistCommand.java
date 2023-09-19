package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Combined;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.player.PlayerGlobalMessagePacket;
import net.moremc.api.nats.packet.client.player.PlayerKickPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.sector.Sector;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.ServerService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistCommand
{
    private final ToolsPlugin toolsPlugin;
    private final API api;

    private final UserService userService;
    private final ServerService serverService;
    private final SectorService sectorService;

    public WhitelistCommand(ToolsPlugin toolsPlugin) {
        this.toolsPlugin = toolsPlugin;
        this.api = API.getInstance();

        this.userService = API.getInstance().getUserService();
        this.serverService = API.getInstance().getServerService();
        this.sectorService = API.getInstance().getSectorService();
    }

    @Command(value = { "whitelist", "bialalista", "wl" }, description = "Uruchamiania lub wylacza biala liste")
    public void handle(@Sender CommandSender player) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                player.sendMessage(MessageHelper.translateText("&8>> &d/wl off &8- &fWyłącza białą liste."));
                player.sendMessage(MessageHelper.translateText("&8>> &d/wl on &8- &fWłącza białą liste."));
                player.sendMessage(MessageHelper.translateText("&8>> &d/wl list &8- &fPokazuje graczy na białej liście."));
                player.sendMessage(MessageHelper.translateText("&8>> &d/wl add <nick> &8- &fDodaje gracza na białą liste."));
                player.sendMessage(MessageHelper.translateText("&8>> &d/wl remove <nick> &8- &fUsuwa gracza z białej listy."));
                player.sendMessage(MessageHelper.translateText("&8>> &d/wl set <reason> &8- &fUstawia powód białej listy."));
            });
        } else {
            player.sendMessage(MessageHelper.translateText("&8>> &d/wl off &8- &fWyłącza białą liste."));
            player.sendMessage(MessageHelper.translateText("&8>> &d/wl on &8- &fWłącza białą liste."));
            player.sendMessage(MessageHelper.translateText("&8>> &d/wl list &8- &fPokazuje graczy na białej liście."));
            player.sendMessage(MessageHelper.translateText("&8>> &d/wl add <nick> &8- &fDodaje gracza na białą liste."));
            player.sendMessage(MessageHelper.translateText("&8>> &d/wl remove <nick> &8- &fUsuwa gracza z białej listy."));
            player.sendMessage(MessageHelper.translateText("&8>> &d/wl set <reason> &8- &fUstawia powód białej listy."));
        }
    }
    @Command(value = { "whitelist on", "wl on" }, description = "Włącza białą liste")
    public void handleOnline(@Sender CommandSender player) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                this.serverService.findByValueOptional(1).ifPresent(server -> {
                    server.getWhitelist().setStatus(true);
                    this.serverService.synchronize(server, SynchronizeType.UPDATE);

                    api.getNatsMessengerAPI().sendPacket("moremc_global", new PlayerGlobalMessagePacket("&c&lWHITELIST &8>> &fNa serwerze została &awłączona &fbiała lista przez&8: &c" + player.getName(), ""));
                    for (Sector sector : this.sectorService.getMap().values()) {
                        for (String nickName : sector.getInfo().getPlayerList()) {
                            api.getNatsMessengerAPI().sendPacket("moremc_proxies", new PlayerKickPacket(nickName, "&cNa serwerze została włączona biała lista."));
                        }
                    }
                });
            });
        }else{
            this.serverService.findByValueOptional(1).ifPresent(server -> {
                server.getWhitelist().setStatus(true);
                this.serverService.synchronize(server, SynchronizeType.UPDATE);

                api.getNatsMessengerAPI().sendPacket("moremc_global", new PlayerGlobalMessagePacket("&c&lWHITELIST &8>> &fNa serwerze została &awłączona &fbiała lista przez&8: &c" + player.getName(), ""));
                for (Sector sector : this.sectorService.getMap().values()) {
                    for (String nickName : sector.getInfo().getPlayerList()) {
                        api.getNatsMessengerAPI().sendPacket("moremc_proxies", new PlayerKickPacket(nickName, "&cNa serwerze została włączona biała lista."));
                    }
                }
            });
        }
    }
    @Command(value = { "whitelist off", "wl off" }, description = "Wyłącza białą liste")
    public void handleOffline(@Sender CommandSender player) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                this.serverService.findByValueOptional(1).ifPresent(server -> {
                    server.getWhitelist().setStatus(false);
                    api.getNatsMessengerAPI().sendPacket("moremc_global", new PlayerGlobalMessagePacket("&c&lWHITELIST &8>> &fNa serwerze została &cwyłączona &fbiała lista przez&8: &c" + player.getName(), ""));
                    this.serverService.synchronize(server, SynchronizeType.UPDATE);
                });
            });
        }else{
            this.serverService.findByValueOptional(1).ifPresent(server -> {
                server.getWhitelist().setStatus(false);
                api.getNatsMessengerAPI().sendPacket("moremc_global", new PlayerGlobalMessagePacket("&c&lWHITELIST &8>> &fNa serwerze została &cwyłączona &fbiała lista przez&8: &c" + player.getName(), ""));
                this.serverService.synchronize(server, SynchronizeType.UPDATE);
            });
        }
    }
    @Command(value = { "whitelist add", "wl add" }, description = "Dodaje gracza na biała liste")
    public void handleAddMember(@Sender CommandSender player, @Name("nick") String nickName) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                this.serverService.findByValueOptional(1).ifPresent(server -> {
                    if (server.getWhitelist().getMembers().contains(nickName)) {
                        throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cGracz&8: &7" + nickName + " &cznajduje się już na białej liscie."));
                    }
                    server.getWhitelist().getMembers().add(nickName);
                    player.sendMessage(MessageHelper.colored("&c&lWHITELIST &8>> &7Pomyślnie dodano gracza&8: &a" + nickName + " &7na białą liste."));
                    this.serverService.synchronize(server, SynchronizeType.UPDATE);
                });
            });
        }else{
            this.serverService.findByValueOptional(1).ifPresent(server -> {
                if (server.getWhitelist().getMembers().contains(nickName)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cGracz&8: &7" + nickName + " &cznajduje się już na białej liscie."));
                }
                server.getWhitelist().getMembers().add(nickName);
                player.sendMessage(MessageHelper.colored("&c&lWHITELIST &8>> &7Pomyślnie dodano gracza&8: &a" + nickName + " &7na białą liste."));
                this.serverService.synchronize(server, SynchronizeType.UPDATE);
            });
        }
    }
    @Command(value = { "whitelist remove", "wl remove" }, description = "Usuwa gracza z białej listy")
    public void handleRemoveMember(@Sender CommandSender player, @Name("nick") String nickName) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                this.serverService.findByValueOptional(1).ifPresent(server -> {
                    if (!server.getWhitelist().getMembers().contains(nickName)) {
                        throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cGracz&8: &7" + nickName + " &cnie znajduje się na białej liscie."));
                    }
                    server.getWhitelist().getMembers().remove(nickName);
                    player.sendMessage(MessageHelper.colored("&c&lWHITELIST &8>> &7Pomyślnie usunięto gracza&8: &a" + nickName + " &7na białą liste."));
                    this.serverService.synchronize(server, SynchronizeType.UPDATE);
                });
            });
        }else{
            this.serverService.findByValueOptional(1).ifPresent(server -> {
                if (!server.getWhitelist().getMembers().contains(nickName)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cGracz&8: &7" + nickName + " &cnie znajduje się na białej liscie."));
                }
                server.getWhitelist().getMembers().remove(nickName);
                player.sendMessage(MessageHelper.colored("&c&lWHITELIST &8>> &7Pomyślnie usunięto gracza&8: &a" + nickName + " &7na białą liste."));
                this.serverService.synchronize(server, SynchronizeType.UPDATE);
            });
        }
    }
    @Command(value = { "whitelist list", "wl list" }, description = "Pokazuje graczy na białej liście")
    public void handleList(@Sender CommandSender player) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                this.serverService.findByValueOptional(1).ifPresent(server -> {
                    player.sendMessage(MessageHelper.colored("&7Na białej liście znajduje się&8: "));
                    for (String nickName : server.getWhitelist().getMembers()) {
                        player.sendMessage(MessageHelper.colored("&8- &d" + nickName));
                    }
                });
            });
        }else{
            this.serverService.findByValueOptional(1).ifPresent(server -> {
                player.sendMessage(MessageHelper.colored("&7Na białej liście znajduje się&8: "));
                for (String nickName : server.getWhitelist().getMembers()) {
                    player.sendMessage(MessageHelper.colored("&8- &d" + nickName));
                }
            });
        }
    }
    @Command(value = { "whitelist set", "wl set" }, description = "Ustawia powód białej listy")
    public void handleSetReason(@Sender CommandSender player, @Name("reason") @Combined  String message) {

        if (player instanceof Player) {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                if (!UserGroupType.hasPermission(UserGroupType.ADMIN, user)) {
                    throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
                }
                this.serverService.findByValueOptional(1).ifPresent(server -> {
                    if (message.trim().isEmpty()) {
                        throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
                    }
                    server.getWhitelist().setReason(message);
                    this.serverService.synchronize(server, SynchronizeType.UPDATE);
                    player.sendMessage(MessageHelper.colored("&c&lWHITELIST &8>> &7Pomyślnie ustawiono powód białej listy&8: &a" + message));
                });
            });
        }else{
            this.serverService.findByValueOptional(1).ifPresent(server -> {
                if (message.trim().isEmpty()) {
                    throw new BladeExitMessage(MessageHelper.colored("&cAby wysłać wiadmość należy podać treść!"));
                }
                server.getWhitelist().setReason(message);
                this.serverService.synchronize(server, SynchronizeType.UPDATE);
                player.sendMessage(MessageHelper.colored("&c&lWHITELIST &8>> &7Pomyślnie ustawiono powód białej listy&8: &a" + message));
            });
        }
    }
}
