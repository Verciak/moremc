package net.moremc.guilds.utilities;

import net.moremc.api.API;
import net.moremc.api.bukkit.BlockState;
import net.moremc.api.entity.guild.regeneration.GuildRegenerationType;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.guilds.GuildsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuildRegenerationUtilities {


    public static void startRegeneration(String guildName) {

        API.getInstance().getGuildService().findByValueOptional(guildName).ifPresent(guild -> {
            guild.getRegeneration().setRegenerationType(GuildRegenerationType.START);

            new BukkitRunnable() {

                int blocks = 0;
                final int x = guild.getRegeneration().getBlockStateList().size();

                @Override
                public void run() {

                    if (guild.getRegeneration().getRegenerationType().equals(GuildRegenerationType.END)) {
                        this.cancel();
                        return;
                    }

                    long delta = Math.abs(blocks - x);
                    long time = TimeUnit.SECONDS.toMillis(delta);

                    guild.getRegeneration().setTimeLeft(time);

                    for (int idx = 0; idx < 5; idx++) {
                        BlockState blockState = takeBlock(guild.getRegeneration().getBlockStateList());
                        if (blockState == null) {
                            guild.sendMessage("&d&lGILDIA&8(&f&lREGENERACJA &8->> &aZakonczono pomyslnie prace&8)");
                            guild.setProtectionTime(System.currentTimeMillis() + TimeType.DAY.getTime(1));
                            guild.getRegeneration().setRegenerationType(GuildRegenerationType.END);
                            guild.getRegeneration().setBlocksPlace(0);
                            guild.synchronize(SynchronizeType.UPDATE);
                            this.cancel();
                            break;
                        }
                        Location location = new Location(Bukkit.getWorld(blockState.getLocation().getWorld()), blockState.getLocation().getX(), blockState.getLocation().getY(), blockState.getLocation().getZ());
                        update(location, blockState);
                        blocks += 1;
                    }
                }
            }.runTaskTimer(GuildsPlugin.getInstance(), 1, 20L);
        });
    }

    public static BlockState takeBlock(List<BlockState> blockStateList) {
        if (blockStateList.isEmpty()) return null;
        BlockState blockState = blockStateList.get(0);
        if (blockState != null) {
            blockStateList.remove(0);
            return blockState;
        }
        return null;
    }

    public static void update(Location location, BlockState blockState) {
        location.getBlock().setType(Material.valueOf(blockState.getMaterialName().toUpperCase()));
        location.getBlock().setTypeIdAndData(Material.valueOf(blockState.getMaterialName()).getId(), blockState.getData(), true);
    }
}
