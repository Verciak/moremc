package net.moremc.proxy.auth.plugin.handler;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.moremc.api.API;
import net.moremc.api.entity.account.Account;
import net.moremc.api.service.entity.AccountService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PreLoginHandler implements Listener
{

    private final AccountService accountService = API.getInstance().getAccountService();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPreLogin(PreLoginEvent event) throws IOException {
        String nickName = event.getConnection().getName();
        PendingConnection connection = event.getConnection();
        if(!this.accountService.findByValueOptional(nickName).isPresent()){
            Account account = this.accountService.findOrCreate(nickName, new Account(nickName, connection.getAddress().getAddress().toString()));
            if(isPremium(nickName)){
                account.setPremium(true);
            }else {
                account.setPremium(false);
            }
        }
        this.accountService.findByValueOptional(nickName).ifPresent(account -> {
            connection.setOnlineMode(account.isPremium());
        });
    }

    public static boolean isPremium(String name) {
        try {
            return ((HttpURLConnection) new URL("https://api.ashcon.app/mojang/v2/user/" + name)
                    .openConnection())
                    .getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
