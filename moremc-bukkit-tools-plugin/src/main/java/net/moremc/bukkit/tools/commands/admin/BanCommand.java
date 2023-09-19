package net.moremc.bukkit.tools.commands.admin;

import com.google.gson.Gson;
import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.ban.Ban;
import net.moremc.api.entity.user.User;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.nats.packet.ban.sync.BanSynchronizeInformationPacket;
import net.moremc.api.nats.packet.ban.type.BanType;
import net.moremc.api.nats.packet.client.player.PlayerGlobalMessagePacket;
import net.moremc.api.nats.packet.client.player.PlayerKickPacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.BanService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import org.bukkit.entity.Player;

import java.util.Optional;

public class BanCommand
{

    private final ToolsPlugin plugin;
    private final API api;

    private final UserService userService;
    private final BanService banService;

    public BanCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.api = API.getInstance();
        this.userService = API.getInstance().getUserService();
        this.banService = API.getInstance().getBanService();
    }

    @Command(value = { "ban" }, description = "")
    public void handle(@Sender Player player, @Name("name") final String name, @Name("reason") final String reason, @Name("time") final String time) {
        this.userService.findByValueOptional(player.getName()).ifPresent(userg -> {

            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, userg)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            Optional<User> user = api.getUserService().findByValueOptional(name);
            if (!user.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
            }
            Optional<Ban> ban = api.getBanService().findByValueOptional(name);
            if(ban.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cjest już zbanowany!"));
            }
            if(reason.trim().isEmpty()) {
                throw new BladeExitMessage(MessageHelper.colored("&cPodaj powód bana!"));
            }
            long delay = DataHelper.parseDateDiff(time, true);

            Ban createBan = banService.create(name, player.getName(), "", reason, delay);

            api.getNatsMessengerAPI().sendPacket("moremc_global_channel", new PlayerGlobalMessagePacket("&cGracz &7" + name + " &czostał &7zbanowany przez &7" + player.getName(), ""));
            api.getNatsMessengerAPI().sendPacket("moremc_proxies_proxy01", new PlayerKickPacket(user.get().getNickName(), "&cZostałeś zabanowany!"));

            api.getNatsMessengerAPI().sendPacket("moremc_master_controller",new BanSynchronizeInformationPacket(createBan.getNickName(), SynchronizeType.CREATE, BanType.NICKNAME, new Gson().toJson(createBan)));
        });
    }
    @Command(value = {"unban"}, description = " Odbanowuje gracz")
    public void handleUnBan(@Sender Player player, @Name("name") final String name) {
        this.userService.findByValueOptional(player.getName()).ifPresent(guser -> {

            if (!UserGroupType.hasPermission(UserGroupType.ADMIN, guser)) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie posiadasz dostępu do tej komendy."));
            }
            Optional<User> user = api.getUserService().findByValueOptional(name);
            if (!user.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie został znaleziony w bazie danych!"));
            }
            Optional<Ban> ban = api.getBanService().findByValueOptional(name);
            if(!ban.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + name + " &cnie jest zbanowany!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_global_channel", new PlayerGlobalMessagePacket("&cGracz &7" + ban.get().getNickName() + " &czostał &7odbanowany przez &7" + player.getName(), ""));
            api.getNatsMessengerAPI().sendPacket("moremc_master_controller",new BanSynchronizeInformationPacket(ban.get().getNickName(), SynchronizeType.REMOVE, BanType.NICKNAME, new Gson().toJson(ban)));
        });
    }
}
