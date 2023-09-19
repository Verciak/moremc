package net.moremc.proxy.auth.plugin.runnable;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.moremc.api.API;
import net.moremc.api.service.entity.AccountService;
import net.moremc.api.service.entity.QueueService;
import net.moremc.api.service.entity.SectorService;
import net.moremc.proxy.auth.plugin.AuthPlugin;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlayerQueueRunnable implements Runnable
{
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    private final QueueService queueService = AuthPlugin.getInstance().getQueueService();
    private final AccountService accountService = API.getInstance().getAccountService();
    private final SectorService sectorService = API.getInstance().getSectorService();

   private final List<String> sectorTitleList = Arrays.asList(
            "&d>&d>&f> &fTrwa &drestartowanie twojego &bsektora&a. &d<&d<&f<",
            "&d<&d<&f< &fTrwa &5restartowanie &ftwojego &csektora&a.. &d>&d>&f>",
            "&a>&5>&d> &fTrwa restartowanie twojego sektora&a... &d<&5<&a<",
            "&fJestes w kolejce: &8(&d{POSITION}&7/&5{QUEUE_SIZE}&8)");

    private int indexList = 0;

    @Override
    public void run() {
        for (ProxiedPlayer onlinePlayer : AuthPlugin.getInstance().getProxy().getPlayers()) {
            for (LinkedList<String> value : this.queueService.getMap().values()) {
                if (!onlinePlayer.getServer().getInfo().getName().equalsIgnoreCase("limbo01")){
                    value.remove(onlinePlayer.getName());
                    continue;
                }
                if (!onlinePlayer.getServer().getInfo().getName().equalsIgnoreCase("limbo01")) continue;
                this.accountService.findByValueOptional(onlinePlayer.getName()).ifPresent(account -> {
                    if (!account.isLogin()) return;

                    this.queueService.findByValueOptional(account.getActualSectorName()).ifPresent(queue -> {

                        if (!queue.contains(onlinePlayer.getName())) {
                            queue.add(onlinePlayer.getName());
                            onlinePlayer.sendMessage(ChatHelper.colored("&aZostałeś pomyślnie dodany do kolejki."));
                        }
                        this.sectorService.findByValueOptional(account.getActualSectorName()).ifPresent(sector -> {
                            if (!sector.getInfo().isOnline()) {

                                AuthPlugin.getInstance().getProxy().createTitle()
                                        .title(new TextComponent(ChatHelper.colored("")))
                                        .subTitle(new TextComponent(ChatHelper.colored("")))
                                        .send(onlinePlayer);

                                indexList++;
                                if (this.indexList >= sectorTitleList.size()) this.indexList = 0;

                                return;
                            }
                            if (queue.indexOf(onlinePlayer.getName()) <= 1) {
                                ServerInfo serverInfo = AuthPlugin.getInstance().getProxy().getServerInfo(sector.getName());

                                if (serverInfo != null) {
                                    onlinePlayer.connect(serverInfo);
                                    queue.remove(onlinePlayer.getName());
                                }
                            }

                        });

                    });


                });
            }
        }
    }
    public void start() {
        AuthPlugin.getInstance().getProxy().getScheduler().schedule(AuthPlugin.getInstance(), this, 1L, 1L, TimeUnit.SECONDS);
    }
}
