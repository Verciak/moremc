package net.moremc.sectors.helper;

import net.moremc.api.API;
import net.moremc.api.entity.user.User;
import net.moremc.api.entity.user.UserSector;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.user.UserSectorChangeInformationPacket;
import net.moremc.api.sector.Sector;
import net.moremc.api.sector.type.SectorType;
import net.moremc.api.serializer.SerializablePotionEffect;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.serializer.ItemSerializer;
import net.moremc.sectors.SectorPlugin;
import net.moremc.sectors.event.UserSectorChangeEvent;
import net.moremc.sectors.event.handler.UserSectorChangeHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Optional;

public class SectorTransferHelper {


    /**
     * @param player
     * @parm Load data UserSector from transfer change sector
     * @parm user
     */
    public static void loadPlayerData(Player player, User user) {
        UserSector userSector = user.getUserSector();
        if(userSector.getLocationWorldName() == null)return;
        Location location = new Location(Bukkit.getWorld(userSector.getLocationWorldName()), userSector.getLocationX(), userSector.getLocationY(), userSector.getLocationZ(), userSector.getLocationYaw(), userSector.getLocationPitch());
        player.teleport(location);
        userSector.setActualSectorName(SectorPlugin.getInstance().getSectorName());
        userSector.setCanInteract(false);
        userSector.setSectorChange(0);

        player.setFoodLevel((int) userSector.getFoodLevel());
        player.setHealth(userSector.getHealth());
        player.setAllowFlight(userSector.isFlying());
        player.setGameMode(GameMode.valueOf(userSector.getGameModeName().toUpperCase()));


        player.getInventory().setArmorContents(ItemSerializer.encodeItem(userSector.getSerializedInventoryArmorContent()));
        player.getInventory().setContents(ItemSerializer.encodeItem(userSector.getSerializedInventoryContent()));
        player.getEnderChest().setContents(ItemSerializer.encodeItem(userSector.getSerializedInventoryEnderchestContent()));
        player.getInventory().setHeldItemSlot(userSector.getInventoryHeldItemSlot());

        SerializablePotionEffect[] potionEffects = userSector.getSerializablePotionEffects();
        if (potionEffects != null) {
            for (SerializablePotionEffect potionEffect : potionEffects) {
                PotionEffect effect = new PotionEffect(
                        PotionEffectType.getByName(potionEffect.getPotionEffectType()),
                        potionEffect.getDuration(),
                        potionEffect.getAmplifier());
                player.addPotionEffect(effect);
            }
        }
        user.addCooldown("sector_change", System.currentTimeMillis() + TimeType.SECOND.getTime(5));
        userSector.setCanInteract(true);
    }
    /**
     * @parm Call event SectorChangeEvent to player change server sector
     * @param player
     * @param sectorTransfer
     * @param locationSave
     */
    public static void changeSectorPlayer(Player player, Sector sectorTransfer, Location locationSave, Event event) {
        if (sectorTransfer.equals(API.getInstance().getSectorService().getCurrentSector().get())) return;
        if (sectorTransfer.getInfo().getType().equals(SectorType.SPAWN) && API.getInstance().getSectorService().getCurrentSector().get().getInfo().getType().equals(SectorType.SPAWN))
            return;

        API.getInstance().getUserService().findByValueOptional(player.getName()).ifPresent(user -> {
            if (event instanceof PlayerTeleportEvent) {
                PlayerTeleportEvent teleportEvent = (PlayerTeleportEvent) event;

                user.addCooldown("sector_change", 0L);

                if(!user.hasCooldown("sector_change")){
                    teleportEvent.setCancelled(true);
                    player.sendTitle("", MessageHelper.translateText("&fOdczekaj &d" + DataHelper.getTimeToString(user.getCooldownTime("sector_change"))));
                    return;
                }
                if (!user.getUserAntiLogout().hasAntiLogout()) {
                    teleportEvent.setCancelled(true);
                    player.sendMessage(MessageHelper.translateText("&cPodczas walki nie można zmieniać sektora!"));
                    return;
                }
                if (!sectorTransfer.getInfo().isOnline()) {
                    if (sectorTransfer.isSpawn()) {
                        Optional<Sector> sectorOptional = API.getInstance().getSectorService().findRandomSectorOnlineByType(SectorType.SPAWN);
                        if (!sectorOptional.isPresent()) {
                            player.sendMessage(MessageHelper.colored("&cAktualnie wszystkie sektory spawn są offline!"));
                            teleportEvent.setCancelled(true);
                            return;
                        }
                        sectorOptional.ifPresent(sector -> {
                            if (!sector.getInfo().isOnline()) {
                                player.sendMessage(MessageHelper.colored("&cAktualnie wszystkie sektory spawn są offline."));
                                teleportEvent.setCancelled(true);
                                return;
                            }
                            UserSectorChangeHandler userSectorChangeHandler = new UserSectorChangeHandler();
                            userSectorChangeHandler.accept(new UserSectorChangeEvent(player, API.getInstance().getUserService().findByValueOptional(player.getName()), sector, locationSave));
                        });
                        return;
                    }
                    teleportEvent.setCancelled(true);
                    player.sendMessage(MessageHelper.colored("&cSektor &7" + sectorTransfer.getName() + " &cjest aktualnie &7niedostępny!"));;
                    return;
                }
                UserSectorChangeHandler userSectorChangeHandler = new UserSectorChangeHandler();
                userSectorChangeHandler.accept(new UserSectorChangeEvent(player, API.getInstance().getUserService().findByValueOptional(player.getName()), sectorTransfer, locationSave));
            }
            if (event instanceof PlayerMoveEvent) {

                if(!user.hasCooldown("sector_change")){
                    Location locationKnockBack = new Location(player.getWorld(), (sectorTransfer.getInfo().getLocationInfo().getMinX() + sectorTransfer.getInfo().getLocationInfo().getMaxX()) / 2, 80, (sectorTransfer.getInfo().getLocationInfo().getMinZ() + sectorTransfer.getInfo().getLocationInfo().getMaxZ()) / 2);
                    knockBackPlayer(player, locationKnockBack);
                    player.sendTitle("", MessageHelper.translateText("&fOdczekaj: &d" + DataHelper.getTimeToString(user.getCooldownTime("sector_change"))));
                    return;
                }

                if (!user.getUserAntiLogout().hasAntiLogout()) {
                    player.sendMessage(MessageHelper.translateText("&cPodczas walki nie można zmieniać sektora."));
                    Location locationKnockBack = new Location(player.getWorld(), (sectorTransfer.getInfo().getLocationInfo().getMinX() + sectorTransfer.getInfo().getLocationInfo().getMaxX()) / 2, 80, (sectorTransfer.getInfo().getLocationInfo().getMinZ() + sectorTransfer.getInfo().getLocationInfo().getMaxZ()) / 2);
                    knockBackPlayer(player, locationKnockBack);
                    return;
                }

                if (!sectorTransfer.getInfo().isOnline()) {
                    if (sectorTransfer.isSpawn()) {
                        Optional<Sector> sectorOptional = API.getInstance().getSectorService().findRandomSectorOnlineByType(SectorType.SPAWN);
                        if (!sectorOptional.isPresent()) {
                            player.sendMessage(MessageHelper.colored("&cAktualnie wszystkie sektory spawn są offline."));
                            Location locationKnockBack = new Location(player.getWorld(), (sectorTransfer.getInfo().getLocationInfo().getMinX() + sectorTransfer.getInfo().getLocationInfo().getMaxX()) / 2, 80, (sectorTransfer.getInfo().getLocationInfo().getMinZ() + sectorTransfer.getInfo().getLocationInfo().getMaxZ()) / 2);
                            knockBackPlayer(player, locationKnockBack);
                            return;
                        }
                        sectorOptional.ifPresent(sector -> {
                            if (!sector.getInfo().isOnline()) {
                                player.sendMessage(MessageHelper.colored("&cAktualnie wszystkie sektory spawn są offline."));
                                Location locationKnockBack = new Location(player.getWorld(), (sectorTransfer.getInfo().getLocationInfo().getMinX() + sectorTransfer.getInfo().getLocationInfo().getMaxX()) / 2, 80, (sectorTransfer.getInfo().getLocationInfo().getMinZ() + sectorTransfer.getInfo().getLocationInfo().getMaxZ()) / 2);
                                knockBackPlayer(player, locationKnockBack);
                                return;
                            }
                            UserSectorChangeHandler userSectorChangeHandler = new UserSectorChangeHandler();
                            userSectorChangeHandler.accept(new UserSectorChangeEvent(player, API.getInstance().getUserService().findByValueOptional(player.getName()), sector, locationSave));
                        });
                        return;
                    }
                    Location locationKnockBack = new Location(player.getWorld(), (sectorTransfer.getInfo().getLocationInfo().getMinX() + sectorTransfer.getInfo().getLocationInfo().getMaxX()) / 2, 80, (sectorTransfer.getInfo().getLocationInfo().getMinZ() + sectorTransfer.getInfo().getLocationInfo().getMaxZ()) / 2);
                    knockBackPlayer(player, locationKnockBack);
                    player.sendMessage(MessageHelper.colored("&cSektor &7" + sectorTransfer.getName() + " &cjest aktualnie &7niedostępny!"));
                    return;
                }
            }
            UserSectorChangeHandler userSectorChangeHandler = new UserSectorChangeHandler();
            userSectorChangeHandler.accept(new UserSectorChangeEvent(player, API.getInstance().getUserService().findByValueOptional(player.getName()), sectorTransfer, locationSave));
        });
    }

    /**
     * @parm Save userSector data location,inventory, etc.
     * @param player
     * @parm user
     */
    public static void saveDataPlayerSector(Player player, Location location,  User user, boolean change, String sectorName){

        UserSector userSector = user.getUserSector();
        userSector.setLocationX(location.getX());
        userSector.setLocationY(location.getY());
        userSector.setLocationZ(location.getZ());
        userSector.setLocationWorldName(location.getWorld().getName());
        userSector.setLocationPitch(location.getPitch());
        userSector.setLocationYaw(location.getYaw());
        userSector.setActualSectorName(SectorPlugin.getInstance().getSectorName());
        userSector.setFlying(player.getAllowFlight());
        userSector.setFoodLevel(player.getFoodLevel());
        userSector.setHealth(player.getHealth());
        userSector.setInventoryHeldItemSlot(player.getInventory().getHeldItemSlot());
        userSector.setGameModeName(player.getGameMode().name());

        userSector.setSerializedInventoryContent(ItemSerializer.decodeItems(player.getInventory().getContents()));
        userSector.setSerializedInventoryArmorContent(ItemSerializer.decodeItems(player.getInventory().getArmorContents()));
        userSector.setSerializedInventoryEnderchestContent(ItemSerializer.decodeItems(player.getEnderChest().getContents()));


        Collection<PotionEffect> activePotionEffects = player.getActivePotionEffects();
        SerializablePotionEffect[] potionEffects = new SerializablePotionEffect[activePotionEffects.size()];
        int i = 0;
        for (PotionEffect effect : activePotionEffects) {
            SerializablePotionEffect potionEffect = new SerializablePotionEffect();

            potionEffect.setPotionEffectType(effect.getType().getName());
            potionEffect.setAmplifier(effect.getAmplifier());
            potionEffect.setDuration(effect.getDuration());

            potionEffects[i] = potionEffect;
            i++;
        }
        userSector.setSerializablePotionEffects(potionEffects);
        userSector.setActualSectorName(sectorName);
        userSector.setSectorChange(System.currentTimeMillis() + TimeType.SECOND.getTime(5));

        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, user);

        Bukkit.getScheduler().runTaskLaterAsynchronously(SectorPlugin.getInstance(), () -> {
            if(change){
               SectorTransferHelper.connect(player, sectorName);
            }
        }, 5L);
    }

    /**
     * @parm Player change server from proxy-servers
     * @param player
     * @param sectorName
     */
    public static void connect(Player player, String sectorName) {
        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_proxies_channel", new UserSectorChangeInformationPacket(player.getName(),
                sectorName));
    }
    public static void knockBackPlayer(Player player, Location location) {
        Location locationVector = player.getLocation().subtract(location);
        double distance = player.getLocation().distance(location);
        if ((1.0 / distance <= 0)) return;
        if (distance <= 0) return;
        player.setVelocity(locationVector.toVector().add(new Vector(0, 0.10, 0)).multiply(1.20 / distance));
    }
}
