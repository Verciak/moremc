package net.moremc.guilds.listeners.player;

import net.moremc.guilds.GuildsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.user.type.UserSettingMessageType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.sector.Sector;
import net.moremc.api.sector.type.SectorType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.utilities.RandomUtilities;
import net.moremc.bukkit.tools.utilities.ItemUtilities;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PlayerDeathEventHandler implements Listener {


    private final UserService userService = API.getInstance().getUserService();
    private final GuildService guildService = API.getInstance().getGuildService();
    private final SectorService sectorService = API.getInstance().getSectorService();

    private final List<ItemStack> itemStackList = Arrays.asList(
            new ItemStack(Material.STONE_PICKAXE),
            new ItemStack(Material.STONE_AXE),
            new ItemStack(Material.ENDER_CHEST),
            new ItemStack(Material.COOKED_BEEF, 64));

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event){
        event.setDeathMessage(null);
        Player playerEntity = event.getEntity();


        this.userService.findByValueOptional(playerEntity.getName()).ifPresent(userDeath -> {


            if(userDeath.getUserAntiLogout().hasAntiLogout()){
                event.getDrops().clear();
                userDeath.getUserAntiLogout().setAttackerNickName("null");
            }

            userDeath.getUserAntiLogout().setAntiLogoutTime(0);

            Player playerKiller = Bukkit.getPlayerExact(userDeath.getUserAntiLogout().getAttackerNickName());
              playerEntity.spigot().respawn();


            Bukkit.getScheduler().runTaskLater(GuildsPlugin.getInstance(), () -> {
                ItemUtilities.addItem(playerEntity, itemStackList);

                Sector sector = this.sectorService.findSectorOnlineByType(SectorType.SPAWN);
                if(sector == null) {
                    playerEntity.sendMessage(MessageHelper.colored("&cNie udało się nawiązać połączenia z sektorem: &7spawn"));
                    playerEntity.sendMessage(MessageHelper.colored(" &cTrwa wyszukiwanie nowego sektora, może to zająć chwile!"));

                    Sector sectorFind = this.sectorService.findSectorOnlineByType(SectorType.GUILD);
                    if (sectorFind == null) {
                        playerEntity.sendMessage(MessageHelper.colored("&4&l:: &c&lAWARIA &4&l::\n&cWszystkie sektory offline!!!!"));
                        return;
                    }
                    int x = RandomUtilities.getRandInt(Math.min(sectorFind.getInfo().getLocationInfo().getMinX(), sectorFind.getInfo().getLocationInfo().getMaxX()), Math.max(sectorFind.getInfo().getLocationInfo().getMinX(), sectorFind.getInfo().getLocationInfo().getMaxX()));
                    int z = RandomUtilities.getRandInt(Math.min(sectorFind.getInfo().getLocationInfo().getMinZ(), sectorFind.getInfo().getLocationInfo().getMaxZ()), Math.max(sectorFind.getInfo().getLocationInfo().getMinZ(), sectorFind.getInfo().getLocationInfo().getMaxZ()));
                    double y = playerEntity.getWorld().getHighestBlockYAt(x, z) + 1.5f;
                    playerEntity.sendMessage(MessageHelper.colored("&fZnaleziono nowy sektor: &d" + sectorFind.getName()));
                    playerEntity.sendMessage(MessageHelper.colored("&fWysłano pakiet prośby o teleportacje do: &d" + sectorFind.getName()));
                    playerEntity.teleport(new Location(playerEntity.getWorld(), x, y, z));
                    return;
                }
                playerEntity.sendMessage(MessageHelper.colored("&fWysłano pakiet prośby o teleportacj do: &d" + sector.getName()));
                playerEntity.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
            }, 5L);


            if(playerKiller == null){
                userDeath.getUserAntiLogout().setAttackerNickName("null");
                userDeath.getUserStatics().removePoints(1);
                this.userService.synchronizeUser(SynchronizeType.UPDATE, userDeath);

                Bukkit.getOnlinePlayers().forEach(playerOnline -> this.userService.findByValueOptional(playerOnline.getName()).ifPresent(user -> {
                    if (user.findUserSettingByType(UserSettingMessageType.DEATH).isStatus()) {
                        ClientSendMessagePacket sendMessagePacket = new ClientSendMessagePacket("&7Gracz &c" + playerEntity.getName() + " &7popełnił samobójstwo &8[&c-1&8]",
                                SendMessageReceiverType.PLAYER, SendMessageType.CHAT);
                        sendMessagePacket.setNickNameTarget(user.getNickName());
                        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", sendMessagePacket);
                    }
                }));
                return;
            }

            ItemUtilities.addItem(playerKiller, event.getDrops());
            event.getDrops().clear();

            this.userService.findByValueOptional(playerKiller.getName()).ifPresent(userKiller -> {
                Bukkit.getScheduler().runTaskLaterAsynchronously(GuildsPlugin.getInstance(), () -> {



                    int userPointsAdd = (int) (40 + (userDeath.getUserStatics().getPoints() - userKiller.getUserStatics().getPoints()) * 0.2),
                            userPointsRemove = ((userPointsAdd / 6) * 2);

                    Optional<GuildImpl> guildEntityOptional = this.guildService.findGuildByNickName(playerEntity.getName());
                    Optional<GuildImpl> guildKillerOptional = this.guildService.findGuildByNickName(playerKiller.getName());

                    GuildImpl guildEntity = null;
                    GuildImpl guildKiller = null;

                    if (guildEntityOptional.isPresent()) {
                        guildEntity = guildEntityOptional.get();
                    }
                    if (guildKillerOptional.isPresent()) {
                        guildKiller = guildKillerOptional.get();
                    }

                    GuildImpl finalGuildKiller = guildKiller;
                    GuildImpl finalGuildEntity = guildEntity;

                    playerKiller.sendTitle(MessageHelper.colored("&a&lZabójstwo"), MessageHelper.colored(
                            "&d✞ &fZabiłeś gracza &8(" + (finalGuildEntity != null ? " &7[&c" + finalGuildEntity.getName().toUpperCase() + "&7]" : "") + "&f" + playerEntity.getName() + "&8, &a+" + userPointsAdd + "&8) &d✞" ));


                    Bukkit.getOnlinePlayers().forEach(playerOnline -> this.userService.findByValueOptional(playerOnline.getName()).ifPresent(user -> {
                        if (user.findUserSettingByType(UserSettingMessageType.DEATH).isStatus()) {
                            ClientSendMessagePacket sendMessagePacket = new ClientSendMessagePacket(
                                    (finalGuildKiller != null ? " &8[&c" + finalGuildKiller.getName().toUpperCase() + "&8]" : "")
                                            + "&7" + userKiller.getFakeNickname()+ "&8(&f+" + userPointsAdd + "&8) &czabił gracza: " + (finalGuildEntity != null ? " &8[&c" + finalGuildEntity.getName().toUpperCase() + "&8]" : "")
                                            + "&7" + userDeath.getFakeNickname() + "&8(&f-" + userPointsRemove + "&7, &c&l" + new DecimalFormat("##.##").format(playerKiller.getHealth() / 2) + "❤&8)",
                                    SendMessageReceiverType.PLAYER, SendMessageType.CHAT);
                            sendMessagePacket.setNickNameTarget(user.getNickName());
                            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", sendMessagePacket);
                        }
                    }));


                    userKiller.getUserStatics().addKills(1);
                    userDeath.getUserStatics().addDeaths(1);

                    userKiller.getUserStatics().addPoints(userPointsAdd);
                    userKiller.getUserAntiLogout().setAttackerNickName("null");
                    userDeath.getUserStatics().removePoints(userPointsRemove);
                    userDeath.getUserAntiLogout().setAttackerNickName("null");

                    this.userService.synchronizeUser(SynchronizeType.UPDATE, userDeath);
                    this.userService.synchronizeUser(SynchronizeType.UPDATE, userKiller);

                    if (guildKiller != null) {
                        guildKiller.addKills(1);
                        guildKiller.addPoints(userPointsAdd);
                        guildKiller.synchronize(SynchronizeType.UPDATE);
                    }
                    if (guildEntity != null) {
                        guildEntity.addDeaths(1);
                        guildEntity.removePoints(userPointsRemove);
                        guildEntity.synchronize(SynchronizeType.UPDATE);
                    }


                }, 10L);
            });
        });
    }
}
