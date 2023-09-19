package net.moremc.proxy.auth.plugin.handler;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.moremc.api.API;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.AccountService;
import net.moremc.api.service.entity.BanService;
import net.moremc.api.service.entity.MasterService;
import net.moremc.api.service.entity.ServerService;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;

import java.util.regex.Pattern;

public class ServerConnectHandler implements Listener {

    private final AccountService accountService = API.getInstance().getAccountService();
    private final MasterService masterService = API.getInstance().getMasterService();
    private final ServerService serverService = API.getInstance().getServerService();
    private final BanService banService = API.getInstance().getBanService();

    @EventHandler
    public void onServerConnect(ServerConnectEvent event){

        ProxiedPlayer player = event.getPlayer();

        if (!Pattern.compile("^[A-Za-z0-9_]{3,16}$").matcher(player.getName()).find()) {
            player.disconnect(ChatHelper.colored("&cNiepoprawny nick!"));
            return;
        }
        this.accountService.findByValueOptional(player.getName()).ifPresent(account -> {
            if(account.getUuid() == null){
                account.setUuid(player.getUniqueId());
                player.disconnect(ChatHelper.colored(
                        "     &2&lLOGOWANIE" +
                                "\n&aAutoryzacja przebiegła pomyślnie.\n" +
                                "      &7Wejdż ponownie."));
            }
        });

        if(!masterService.isConnected()) {
            player.disconnect(ChatHelper.colored(
                    "      \n" +
                            " &cSerwer głowny od aplikacji nie odpowiada od &7" + DataHelper.getTimeToString(masterService.getLastHeartbeat()) + "\n" +
                            " &cTrzeba poczekać az zostanie on ponownie uruchomiony.\n" +
                            "      "
            ));
            return;
        }
        this.serverService.findByValueOptional(1).ifPresent(server -> {
            if(!server.getWhitelist().getMembers().contains(player.getName()) && server.getWhitelist().isStatus()){
                player.disconnect(ChatHelper.colored(
                        " &cNa serwerze aktualnie jest włączona biała lista \n&7" +
                                server.getWhitelist().getReason()));
            }
        });
        this.banService.findByValueOptional(player.getName()).ifPresent(ban -> {
            player.disconnect(ChatHelper.colored(
                    "                 &cZostałeś zbanowany \n" +
                            "      \n" +
                            " &cAdmininstrator: &7" + ban.getAdminNickName() + " \n" +
                            " &cPowód: &7" + ban.getReason() + " \n" +
                            "      \n"
            ));
        });
    }
}
