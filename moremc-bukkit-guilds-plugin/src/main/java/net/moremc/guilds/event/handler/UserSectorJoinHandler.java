package net.moremc.guilds.event.handler;

import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.user.UserSectorUpdateInformationPacket;
import net.moremc.api.sector.Sector;
import net.moremc.api.sector.type.SectorType;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.utilities.RandomUtilities;
import net.moremc.bukkit.tools.utilities.ItemUtilities;
import net.moremc.guilds.GuildsPlugin;
import net.moremc.guilds.event.UserSectorJoinEvent;
import net.moremc.guilds.inventory.create.GuildAcceptFreeLocationInventory;
import net.moremc.guilds.inventory.create.GuildCreateInventory;
import net.moremc.guilds.service.TeamService;
import net.moremc.sectors.SectorPlugin;
import net.moremc.sectors.helper.SectorTransferHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class UserSectorJoinHandler implements Consumer<UserSectorJoinEvent> {

    private final UserService userService = API.getInstance().getUserService();
    private final GuildAcceptFreeLocationInventory acceptFreeLocationInventory = new GuildAcceptFreeLocationInventory();
    private final GuildCreateInventory guildCreateInventory = new GuildCreateInventory();
    private final TeamService teamService = GuildsPlugin.getInstance().getTeamService();
    private final SectorService sectorService = API.getInstance().getSectorService();

    private final List<ItemStack> itemStackList = Arrays.asList(
            new ItemStack(Material.STONE_PICKAXE),
            new ItemStack(Material.STONE_AXE),
            new ItemStack(Material.ENDER_CHEST),
            new ItemStack(Material.COOKED_BEEF, 64));

    @Override
    public void accept(UserSectorJoinEvent userSectorJoinEvent) {
        Player player = userSectorJoinEvent.getPlayer();

        if(!this.userService.findByValueOptional(player.getName()).isPresent()){
            this.userService.findOrCreate(player.getName(), new User(player.getName(), player.getUniqueId()));
           ItemUtilities.addItem(player, itemStackList);


            Sector sectorFind = this.sectorService.findSectorOnlineByType(SectorType.GUILD);
            if (sectorFind == null) {
                player.sendMessage(MessageHelper.colored("&4Błąd: &cNie udało się nawiązać połączenia z sektorem: &4gildyjnym"));
                player.sendMessage(MessageHelper.colored("&aTrwa wyszukiwanie nowego sektora spawn..."));

                Sector sector = this.sectorService.findSectorOnlineByType(SectorType.SPAWN);
                if(sector == null){
                    player.sendMessage(MessageHelper.colored("&4&l:: &c&lAWARIA &4&l::\n&cWszystkie sektory offline!!!!"));
                    return;
                }
                player.sendMessage(MessageHelper.colored("&fWysłano pakiet prośby o teleportacj do: &d" + sector.getName()));
                player.teleport(new Location(Bukkit.getWorld("world"), 0, 70, 0));
                return;
            }
            int x = RandomUtilities.getRandInt(Math.min(sectorFind.getInfo().getLocationInfo().getMinX(), sectorFind.getInfo().getLocationInfo().getMaxX()), Math.max(sectorFind.getInfo().getLocationInfo().getMinX(), sectorFind.getInfo().getLocationInfo().getMaxX()));
            int z = RandomUtilities.getRandInt(Math.min(sectorFind.getInfo().getLocationInfo().getMinZ(), sectorFind.getInfo().getLocationInfo().getMaxZ()), Math.max(sectorFind.getInfo().getLocationInfo().getMinZ(), sectorFind.getInfo().getLocationInfo().getMaxZ()));
            double y = player.getWorld().getHighestBlockYAt(x, z) + 1.5f;
            player.sendMessage(MessageHelper.colored("&fZnaleziono nowy sektor: &d" + sectorFind.getName()));
            player.sendMessage(MessageHelper.colored("&fWysłano pakiet prośby o teleportacje do: &d" + sectorFind.getName()));
            player.teleport(new Location(player.getWorld(), x, y, z));
        }

        Optional<User> optionalUser = this.userService.findByValueOptional(player.getName());
        optionalUser.ifPresent(user -> {
            user.getUserSector().setActualSectorName(API.getInstance().getSectorName());
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_proxies_auth_channel",
                    new UserSectorUpdateInformationPacket(player.getName(), API.getInstance().getSectorName()));

            this.teamService.initializeNameTag(player, user);

            if (player.getHealth() <= 0) {
                player.spigot().respawn();
                Bukkit.getScheduler().runTaskLater(GuildsPlugin.getInstance(), () -> {
                    ItemUtilities.addItem(player, itemStackList);

                    Sector sector = this.sectorService.findSectorOnlineByType(SectorType.SPAWN);
                    if(sector == null) {
                        player.sendMessage(MessageHelper.colored("&4Błąd: &cNie udało się nawiązać połączenia z sektorem: &4spawn"));
                        player.sendMessage(MessageHelper.colored("&aTrwa wyszukiwanie nowego sektora..."));

                        Sector sectorFind = this.sectorService.findSectorOnlineByType(SectorType.GUILD);
                        if (sectorFind == null) {
                            player.sendMessage(MessageHelper.colored("&4&l:: &c&lAWARIA &4&l::\n&cWszystkie sektory offline!!!!"));
                            return;
                        }
                        int x = RandomUtilities.getRandInt(Math.min(sectorFind.getInfo().getLocationInfo().getMinX(), sectorFind.getInfo().getLocationInfo().getMaxX()), Math.max(sectorFind.getInfo().getLocationInfo().getMinX(), sectorFind.getInfo().getLocationInfo().getMaxX()));
                        int z = RandomUtilities.getRandInt(Math.min(sectorFind.getInfo().getLocationInfo().getMinZ(), sectorFind.getInfo().getLocationInfo().getMaxZ()), Math.max(sectorFind.getInfo().getLocationInfo().getMinZ(), sectorFind.getInfo().getLocationInfo().getMaxZ()));
                        double y = player.getWorld().getHighestBlockYAt(x, z) + 1.5f;
                        player.sendMessage(MessageHelper.colored("&fZnaleziono nowy sektor: &d" + sectorFind.getName()));
                        player.sendMessage(MessageHelper.colored("&fWysłano pakiet prośby o teleportacje do: &d" + sectorFind.getName()));
                        player.teleport(new Location(player.getWorld(), x, y, z));
                        return;
                    }
                    player.sendMessage(MessageHelper.colored("&fWysłano pakiet prośby o teleportacj do: &d" + sector.getName()));
                    player.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
                }, 5L);
            }

            if (user.isGuildCreatedLeave()) {
                user.setViewTerrainGuild(false);
                user.setGuildCreatedLeave(false);
                user.setViewTerrainGuildTime(0L);
                player.sendTitle(MessageHelper.translateText("&4?"), MessageHelper.translateText("&cWyszedłeś podczas wybierania terenu..."));
                player.sendMessage(MessageHelper.translateText("&4???? &c&l:: &fSpróbuj ponownie &c/g konfiguracja"));
                Bukkit.getScheduler().runTaskLater(SectorPlugin.getInstance(), () -> {
                    player.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
                }, 20L);
                return;
            }
            SectorTransferHelper.loadPlayerData(player, user);

            if(user.isGuildCreated()){
                Bukkit.getScheduler().runTaskLater(SectorPlugin.getInstance(), () -> {
                //    this.guildCreateInventory.show(player);
                }, 10L);
                return;
            }
            if(!user.hasViewTerrainGuildTime()){
                Bukkit.getScheduler().runTaskLater(GuildsPlugin.getInstance(), () -> {
                    player.sendTitle(MessageHelper.translateText("&2&lTEREN"), MessageHelper.translateText("&aPodglądaj teren gildii."));
                    player.setAllowFlight(true);
                    player.setFlySpeed(0);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, 3F));
                }, 10L);
                return;
            }
            if(user.isViewTerrainGuild()){
                Bukkit.getScheduler().runTaskLater(SectorPlugin.getInstance(), () -> {
                    this.acceptFreeLocationInventory.show(player);
                }, 10L);
            }
        });
        new MessageHelper(player, MessageHelper.translateText("&dZostałeś &fpomyślnie &dpołączony &fz sektorem&8: &5{NAME}").replace("{NAME}", API.getInstance().getSectorName()))
                .send(SendMessageType.TITLE);

    }
}