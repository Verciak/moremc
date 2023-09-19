package net.moremc.proxy.auth.plugin;

import net.md_5.bungee.api.plugin.Plugin;
import net.moremc.api.API;
import net.moremc.api.nats.packet.account.request.AccountLoadRequestPacket;
import net.moremc.api.nats.packet.sector.request.SectorLoadDataRequestPacket;
import net.moremc.api.nats.packet.whitelist.load.ServerWhitelistLoadRequestPacket;
import net.moremc.api.service.entity.QueueService;
import net.moremc.proxy.auth.plugin.command.AccountLoginCommand;
import net.moremc.proxy.auth.plugin.command.AccountRegisterCommand;
import net.moremc.proxy.auth.plugin.command.AccountUnRegisterCommand;
import net.moremc.proxy.auth.plugin.handler.PostLoginHandler;
import net.moremc.proxy.auth.plugin.handler.PreLoginHandler;
import net.moremc.proxy.auth.plugin.handler.ProxyPingHandler;
import net.moremc.proxy.auth.plugin.handler.ServerConnectHandler;

public class AuthPlugin extends Plugin
{

    private static AuthPlugin instance;

    private QueueService queueService;

    @Override
    public void onEnable() {
        instance = this;
        this.queueService = new QueueService();

        API api = new API(false);
        api.setSectorName("proxy01");
        api.initialize(
                "net.moremc.proxy.auth.plugin.nats",
                "moremc_proxies_auth_channel", "proxy01");
        api.getNatsMessengerAPI().sendPacket("moremc_master_controller", new AccountLoadRequestPacket("proxy01"));
        api.getNatsMessengerAPI().sendPacket("moremc_master_controller", new SectorLoadDataRequestPacket("proxy01"));

        getProxy().getPluginManager().registerCommand(this, new AccountLoginCommand("login"));
        getProxy().getPluginManager().registerCommand(this, new AccountRegisterCommand("register"));
        getProxy().getPluginManager().registerCommand(this, new AccountUnRegisterCommand("unregister"));

        getProxy().getPluginManager().registerListener(this, new PreLoginHandler());
        getProxy().getPluginManager().registerListener(this, new PostLoginHandler());
        getProxy().getPluginManager().registerListener(this, new ProxyPingHandler());
        getProxy().getPluginManager().registerListener(this, new ServerConnectHandler());

//        new PlayerQueueRunnable().start();

        getProxy().getLogger().info("[MOREMC-AUTH-PROXY] Enabled.");

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new ServerWhitelistLoadRequestPacket(api.getSectorName()));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public QueueService getQueueService() {
        return queueService;
    }
    public static AuthPlugin getInstance() {
        return instance;
    }
}
