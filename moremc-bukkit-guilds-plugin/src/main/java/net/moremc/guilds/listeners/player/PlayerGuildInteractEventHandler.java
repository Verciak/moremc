package net.moremc.guilds.listeners.player;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.WorldBorder;
import net.moremc.api.API;
import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.serializer.LocationSerializer;
import net.moremc.api.service.entity.GuildPlayerSelectAreaGeneratorService;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.ArmorStandHelper;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.PlayerCameraHelper;
import net.moremc.bukkit.api.helper.type.SendType;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.guilds.GuildsPlugin;
import net.moremc.guilds.inventory.create.GuildCreateInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Arrays;

public class PlayerGuildInteractEventHandler implements Listener {

    private final GuildService guildService = API.getInstance().getGuildService();
    private final UserService userService = API.getInstance().getUserService();
    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final GuildPlayerSelectAreaGeneratorService playerSelectAreaGeneratorService = API.getInstance().getSelectAreaGeneratorService();
    private final GuildCreateInventory guildCreateInventory = new GuildCreateInventory();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();

        Block block = event.getClickedBlock();
        if (block == null) return;

        this.guildService.findGuildByNickName(player.getName()).ifPresent(guild -> {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {


                ItemStack itemStack = player.getItemInHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                GuildArea guildArea = user.getGuildAreaSelect();
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && guildArea != null && guild.getLocation() == null && itemStack.getType().equals(Material.SKULL_ITEM) && itemMeta != null && itemMeta.getDisplayName().equalsIgnoreCase(MessageHelper.colored("&aWytwarzanie terenu gildyjnego"))) {
                    player.getInventory().removeItem(itemStack);


                    player.sendTitle("", MessageHelper.translateText("&dObserwuj pasek nad ekwipunkiem."));

                    new BukkitRunnable(){

                        int size = 0;

                        @Override
                        public void run() {



                            bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {

                                Location location = new Location(player.getWorld(), guildArea.getX(), 100, guildArea.getZ(), 82, 82);
                                ArmorStandHelper armorStandHelper = bukkitUser.getArmorStandHelperList().get(4);
                                armorStandHelper.setDisplayName(Arrays.asList(""));
                                armorStandHelper.setLocation(location);
                                armorStandHelper.send(SendType.UPDATE, location, "");

                                EntityArmorStand entityArmorStand = (EntityArmorStand) armorStandHelper.getEntity().get(0);
                                entityArmorStand.setGravity(false);
                                PlayerCameraHelper.setCamera(player, entityArmorStand);

                                bukkitUser.updateActionBar(UserActionBarType.GUILD_CREATE,
                                        "&5&lGILDIA &8>> &fTrwa generowanie terenu&8(&d" + size + "&8x&532&f, &d" +
                                                new DecimalFormat("##.##").format((((size) * 1.0) / 32) * 100) + "&5%&8)");

                                borderUpdate(guildArea, player, size);

                            if(size >= 32){
                                this.cancel();
                                PlayerCameraHelper.removeCamera(player);
                                bukkitUser.removeActionBar(UserActionBarType.GUILD_CREATE);
                                player.sendTitle("", MessageHelper.translateText("&aTwój teren został pomyślnie wygenerowany."));
                                guild.setLocation(new LocationSerializer("world", user.getGuildAreaSelect().getX(), 38, user.getGuildAreaSelect().getZ(), 32));
                                guild.setLocationHome(new LocationSerializer("world", user.getGuildAreaSelect().getX(), 38, user.getGuildAreaSelect().getZ(), 32));
                                guild.synchronize(SynchronizeType.UPDATE);
                                player.sendMessage(MessageHelper.colored(
                                        "&aWygenerowano teren twojej gildii dzięki temu twoja gildia pomyślnie została skonfigurowana.\n" +
                                        "&8>> &fPodejrzyj&8: &d/g panel &fwięcej informacji pod&8: &d/gildia"));
                                user.setGuildAreaSelect(null);
                                guildCreateInventory.show(player);
                                return;
                            }

                            size++;
                            });
                        }
                    }.runTaskTimer(GuildsPlugin.getInstance(), 1L, 20L);
                }
            });
        });
        this.guildService.findGuildByLocation(block.getWorld().getName(), block.getX(), block.getZ()).ifPresent(guild -> {
            if(player.getItemInHand().getType().equals(Material.GOLD_AXE) && player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getDisplayName() != null && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(MessageHelper.colored("&8* &5Zaznacz teren &8*")) && ItemIdentityHelper.compareIdentity(player.getItemInHand(), "axe_select")) {
                GuildGeneratorType generatorType = GuildGeneratorType.valueOf(ItemIdentityHelper.getItemIdentityTagName(player.getItemInHand(), "axe_select"));


                if (guild.hasPlayer(player.getName()) == null) return;

                switch (event.getAction()) {
                    case LEFT_CLICK_BLOCK: {
                        if (!guild.findGuildPlayerByNickname(player.getName()).get().getPlayerType().equals(GuildPlayerType.OWNER)) return;
                        guild.findGuildGeneratorByType(generatorType).ifPresent(guildGenerator -> {
                            event.setCancelled(true);
                            Location corner = event.getClickedBlock().getLocation();
                            guildGenerator.setLocationCornerSecond(new LocationSerializer(corner.getWorld().getName(), corner.getBlockX(), corner.getBlockY(), corner.getBlockZ(), 0));
                            player.sendMessage(MessageHelper.translateText("&fPomyślnie &dzaznaczono &fteren &fgeneratora &8(&dLCB&8)."));
                        });
                        break;
                    }
                    case RIGHT_CLICK_BLOCK: {
                        if (!guild.findGuildPlayerByNickname(player.getName()).get().getPlayerType().equals(GuildPlayerType.OWNER)) return;
                        guild.findGuildGeneratorByType(generatorType).ifPresent(guildGenerator -> {
                            event.setCancelled(true);
                            Location corner = event.getClickedBlock().getLocation();
                            guildGenerator.setLocationCornerFirst(new LocationSerializer(corner.getWorld().getName(), corner.getBlockX(), corner.getBlockY(), corner.getBlockZ(), 0));
                            player.sendMessage(MessageHelper.translateText("&fPomyślnie &dzaznaczono &fteren &fgeneratora &8(&dRCB&8)."));
                        });
                        break;
                    }
                }
            }
        });
    }

    private void borderUpdate(GuildArea guildArea, Player player, int size) {
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();
        worldBorder.setSize(size * 2);
        worldBorder.transitionSizeBetween(worldBorder.getSize(), worldBorder.getSize() - 0.5, Long.MAX_VALUE);
        worldBorder.setCenter(guildArea.getX(), guildArea.getZ());
        PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public boolean isOnArea(GuildArea guildArea, String world, int x, int z) {
        if (!world.equals("world")) {
            return false;
        }
        int distancex = Math.abs(x - guildArea.getX());
        int distancez = Math.abs(z - guildArea.getZ());
        return distancex - 1 <= 32 && distancez - 1 <= 32;
    }
}
