package net.moremc.bukkit.tools.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import net.moremc.api.API;
import net.moremc.api.entity.server.Server;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.GuildService;
import net.moremc.api.service.entity.ServerService;
import net.moremc.api.service.entity.UserService;

import java.util.Optional;

public class AsyncPlayerChatEventHandler implements Listener
{

    private final UserService userService = API.getInstance().getUserService();
    private final GuildService guildService = API.getInstance().getGuildService();

    private final ServerService serverService = API.getInstance().getServerService();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        if(event.isCancelled())return;

        event.setCancelled(true);

        Player player =  event.getPlayer();
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            Optional<Server> server = serverService.findByValueOptional(1);
            if(!server.isPresent()) {
                event.setCancelled(true);
                player.sendMessage("Error");
                return;
            }
            if(!server.get().isChatStatus() && !UserGroupType.hasPermission(UserGroupType.HELPER, user)) {
                event.setCancelled(true);
                player.sendMessage("&cChat jest aktualnie wyłączony");
                return;
            }
            String message = event.getMessage().replace("%", "").replace("<3", "❤");

            event.setFormat(user.getGroupType().getChatFormat()
                    .replace("{GUILD}", (this.guildService.findGuildByNickName(user.getNickName()).isPresent() ? "&c" + this.guildService.findGuildByNickName(user.getNickName()).get().getName() : ""))
                    .replace("{MESSAGE}", message)
                    .replace("{PLAYER}", player.getName())
                    .replace("{POINTS}", String.valueOf(user.getUserStatics().getPoints())));

            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel",
                    new ClientSendMessagePacket(event.getFormat(), SendMessageReceiverType.ALL, SendMessageType.CHAT));
        });

    }
}
