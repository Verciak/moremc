package net.moremc.guilds.runnable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.BukkitAPI;
import net.moremc.bukkit.api.cache.BukkitCache;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.helper.type.SendType;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import net.moremc.guilds.GuildsPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ArmorStandUpdateRunnable implements Runnable{


    private final BukkitCache bukkitCache = BukkitAPI.getInstance().getBukkitCache();
    private final GuildService guildService = API.getInstance().getGuildService();
    private static float yaw =0 ;
    private static double height = (float) -0.25;
    private static boolean add = true;

    public void start(){
        Bukkit.getScheduler().runTaskTimer(GuildsPlugin.getInstance(), this, 0L, 0L);
    }

    @Override
    public void run() {
        yaw = (float) (yaw + 3.16);
        if (yaw >= 360) yaw = 0;
        height = height + (add ? 0.025 : -0.025);
        if (height >= 0.25 || height <= -0.25) add = !add;
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.bukkitCache.findBukkitUserByNickName(player.getName()).ifPresent(bukkitUser -> {

                if(!this.guildService.findGuildByLocation(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ()).isPresent()) {
                    bukkitUser.removeActionBar(UserActionBarType.GUILD_REGION);
                }

                this.guildService.findGuildByLocation(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockZ()).ifPresent(guild -> {

                    Location locationGuild = new Location(Bukkit.getWorld("world"), guild.getLocation().getX(),38, guild.getLocation().getZ());

                    bukkitUser.updateActionBar(UserActionBarType.GUILD_REGION, (guild.findGuildPlayerByNickname(player.getName()).isPresent() ?
                            MessageHelper.translateText("&aTeren twojej gildii&8(&a" + guild.getName().toUpperCase() + "&f, &a" +
                                    new DecimalFormat("##.##").format(player.getLocation().distance(locationGuild))  + "m&8)") :

                            MessageHelper.translateText("&cTeren wrogiej gildii&8(&c" + guild.getName().toUpperCase() + "&f, &c" +
                                    new DecimalFormat("##.##").format(player.getLocation().distance(locationGuild)) + "m&8)")));


                    bukkitUser.getArmorStandHelperList().get(0).apply(armorStandHelper -> {
                        if (player.getLocation().distance(locationGuild) <= 10) {
                            armorStandHelper.setDisplayName(Arrays.asList(
                                    (guild.findGuildPlayerByNickname(player.getName()).isPresent() ? "&aJajko należy do twojej gildii" : "&cJajko należy do wrogiej gildii"),
                                    "&fOchrona&8(" + (!guild.getStatics().hasProtection() ? "&a" + DataHelper.getTimeToString(guild.getStatics().getProtection()) : "&cGildia może zostać podbita") + "&8)",
                                    "&fZdrowie&8(&c" + guild.getStatics().getLifeHp() + "&7/&a100&8)",
                                    "&fŻycia&8(" + multiplyLives(guild.getStatics().getLifes()) + "&8)",
                                    "&8[" + (guild.findGuildPlayerByNickname(player.getName()).isPresent() ? "&a" : "&c") + guild.getName().toUpperCase() + "&8]"));


                            armorStandHelper.setLocation(locationGuild);
                            Location location = armorStandHelper.getLocation();
                            location.setYaw(yaw);
                            location.setPitch(yaw - player.getLocation().getPitch());
                            location.setY(32 + height);
                            armorStandHelper.send(SendType.UPDATE, new Location(Bukkit.getWorld("world"), guild.getLocation().getX(), guild.getLocation().getY(), guild.getLocation().getZ()), guild.getHeart().getEggType().getUrl());
                        }else{
                            armorStandHelper.send(SendType.REMOVE, new Location(Bukkit.getWorld("world"), guild.getLocation().getX(), guild.getLocation().getY(), guild.getLocation().getZ()), "");
                            armorStandHelper.setEntity(new ArrayList<>());
                        }
                    });
                });
            });
        });
    }

    private String multiplyLives(int lifes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < lifes; i++){
            stringBuilder.append("&c&l❤");
        }
        return stringBuilder.toString();
    }
}