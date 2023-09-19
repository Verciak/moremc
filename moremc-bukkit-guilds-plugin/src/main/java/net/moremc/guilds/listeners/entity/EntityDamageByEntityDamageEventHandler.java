package net.moremc.guilds.listeners.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Optional;

public class EntityDamageByEntityDamageEventHandler implements Listener {

    private final UserService userService = API.getInstance().getUserService();
    private final GuildService guildService = API.getInstance().getGuildService();


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityDamage(EntityDamageByEntityEvent event){
        if(event.isCancelled())return;
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){

            Player playerDamager = (Player) event.getDamager();
            Player playerEntity = (Player) event.getEntity();
            this.userService.findByValueOptional(playerEntity.getName()).ifPresent(userEntity -> {
                this.userService.findByValueOptional(playerDamager.getName()).ifPresent(userDamager -> {

                    Optional<GuildImpl> guildEntityOptional = this.guildService.findGuildByNickName(playerEntity.getName());
                    Optional<GuildImpl> guildDamagerOptional = this.guildService.findGuildByNickName(playerDamager.getName());

                    GuildImpl guildDamager = null;
                    GuildImpl guildEntity = null;

                    if (guildDamagerOptional.isPresent() && guildEntityOptional.isPresent()) {
                        guildDamager = guildDamagerOptional.get();
                        guildEntity = guildEntityOptional.get();

                    }
                    if (guildEntity != null && guildEntity.equals(guildDamager) && !guildEntity.isFriendlyFire()) {
                        event.setDamage(0);
                        return;
                    }
                    if (userDamager.hasProtection()) {
                        event.setCancelled(true);
                        playerDamager.sendMessage(MessageHelper.colored("&cPosiadasz ochrone startową przez: &7" + DataHelper.getTimeToString(userDamager.getCooldownTime("protection"))));
                        return;
                    }
                    if (userEntity.hasProtection()) {
                        event.setCancelled(true);
                        playerDamager.sendMessage(MessageHelper.colored("&cTen gracz posiada ochrone startową przez: &7" + DataHelper.getTimeToString(userEntity.getCooldownTime("protection"))));
                        return;
                    }
                    userEntity.getUserAntiLogout().setAntiLogoutTime(System.currentTimeMillis() + TimeType.SECOND.getTime(30));
                    userDamager.getUserAntiLogout().setAntiLogoutTime(System.currentTimeMillis() + TimeType.SECOND.getTime(30));
                    userDamager.getUserAntiLogout().setAttackerNickName(userEntity.getNickName());
                    userEntity.getUserAntiLogout().setAttackerNickName(userDamager.getNickName());
                });
            });
        }
    }
}
