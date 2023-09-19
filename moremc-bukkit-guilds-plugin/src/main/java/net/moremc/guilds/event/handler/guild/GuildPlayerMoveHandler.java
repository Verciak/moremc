package net.moremc.guilds.event.handler.guild;

import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.event.guild.GuildPlayerMoveEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class GuildPlayerMoveHandler implements Consumer<GuildPlayerMoveEvent> {


    private final UserService userService = API.getInstance().getUserService();

    @Override
    public void accept(GuildPlayerMoveEvent event) {
        Player player = event.getPlayer();
        Optional<GuildImpl> guildFrom = event.getOptionalGuildFrom();
        Optional<GuildImpl> guildTo = event.getOptionalGuildTo();


        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if (!guildFrom.isPresent() && guildTo.isPresent()) {
            guildTo.ifPresent(guild -> {
                if (guild.hasPlayer(player.getName()) != null) {
                    player.sendTitle(MessageHelper.translateText("&8### &8[&a" + guild.getName().toUpperCase() + "&8] &8###"),
                            MessageHelper.translateText("&a&l• &aWkroczyłeś na teren swojej gildii &a&l•"));
                    return;
                }
                player.sendTitle(MessageHelper.translateText("&8### &8[&c" + guild.getName().toUpperCase() + "&8] &8###"),
                        MessageHelper.translateText("&c&l• &aWkroczyłeś na teren wrogiej gildii &c&l•"));

                if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                    guild.sendMessage("&c&l• &cWróg&8: &4"  + user.getFakeNickname() +  " &cwkorczył na teren twojej gildii &c&l•");
                }
            });
        }
        if (!guildTo.isPresent() && guildFrom.isPresent()) {
            guildFrom.ifPresent(guild -> {
                if (guild.hasPlayer(player.getName()) != null) {
                    player.sendTitle(MessageHelper.translateText("&8### &8[&a" + guild.getName().toUpperCase() + "&8] &8###"),
                            MessageHelper.translateText("&a&l• &aOpuściłeś teren swojej gildii &a&l•"));
                    return;
                }
                player.sendTitle(MessageHelper.translateText("&8### &8[&c" + guild.getName().toUpperCase() + "&8] &8###"),
                        MessageHelper.translateText("&c&l• &aOpusciłeś teren wrogiej gildii &c&l•"));

                if (!UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                    guild.sendMessage("&c&l• &cWróg&8: &4"  + user.getFakeNickname() +  " &copuścił teren twojej gildii &c&l•");
                }
            });
        }
        });
    }
}
