package net.moremc.bukkit.tools.commands.admin;

import me.vaperion.blade.annotation.Command;
import me.vaperion.blade.annotation.Name;
import me.vaperion.blade.annotation.Sender;
import me.vaperion.blade.exception.BladeExitMessage;
import net.moremc.api.API;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.teleport.TeleportPlayerRequestPacket;
import net.moremc.api.sector.Sector;
import net.moremc.api.sector.info.SectorLocationInfo;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;
import net.moremc.sectors.helper.SectorTransferHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class AdminTeleportCommand
{
    private final ToolsPlugin plugin;
    private final API api;

    private final UserService userService;
    private final SectorService service;

    public AdminTeleportCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.api = API.getInstance();

        this.userService = API.getInstance().getUserService();
        this.service = API.getInstance().getSectorService();
    }
    @Command(value = {"teleport", "tp"}, description = "Teleportuje do gracza")
    public void handle(@Sender Player player, @Name("nick") final String nick) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (!api.getSectorService().isOnlinePlayer(nick)) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + nick + " &cjest aktualnie offline!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new TeleportPlayerRequestPacket(player.getName(), nick, 0));
        });
    }

    @Command(value = {"steleport", "stp", "s"}, description = "Teleportuje do gracza do ciebie")
    public void handleS(@Sender Player player, @Name("nick") final String nick) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            if (!api.getSectorService().isOnlinePlayer(nick)) {
                throw new BladeExitMessage(MessageHelper.colored("&cGracz &7" + nick + " &cjest aktualnie offline!"));
            }
            api.getNatsMessengerAPI().sendPacket("moremc_client_channel", new TeleportPlayerRequestPacket(nick, player.getName(), 0));
        });
    }

    @Command(value = {"tpsector"}, description = "Teleportuje na środek sektora")
    public void handleSector(@Sender Player player, @Name("sector") final String sector) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                throw new BladeExitMessage(MessageHelper.colored("&4Błąd: &cNie posiadasz dostępu do tej komendy."));
            }
            Optional<Sector> tpSector = service.findByValueOptional(sector);
            if (!tpSector.isPresent()) {
                throw new BladeExitMessage(MessageHelper.colored("&cNie znaleziono sektora &7" + sector + "&c!"));
            }
            if(!tpSector.get().getInfo().isOnline()) {
                throw new BladeExitMessage(MessageHelper.colored("&cSektor &7" + sector + "&cjest aktualnie &7niedostępny!"));
            }
            if(user.getUserSector().getActualSectorName().equalsIgnoreCase(tpSector.get().getName())) {
                throw new BladeExitMessage(MessageHelper.colored("&cJesteś już połączony z tym sektorem!"));
            }
            SectorTransferHelper.saveDataPlayerSector(player, getCenter(80, tpSector.get()), user, true, tpSector.get().getName());
        });
    }
    public static Location getCenter(int y, Sector sector) {
        SectorLocationInfo tpSector = sector.getInfo().getLocationInfo();
        int centerX = (tpSector.getMinX() + tpSector.getMaxX()) / 2;
        int centerZ = (tpSector.getMinZ() + tpSector.getMaxZ()) / 2;
        return new Location(Bukkit.getWorld(tpSector.getWorldName()), centerX, y, centerZ);
    }
}