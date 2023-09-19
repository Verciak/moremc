package net.moremc.proxy.auth.plugin.handler;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.moremc.api.API;
import net.moremc.api.entity.account.state.AccountState;
import net.moremc.api.service.entity.AccountService;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PostLoginHandler implements Listener
{
    private final AccountService accountService = API.getInstance().getAccountService();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (!this.accountService.findByValueOptional(player.getName()).isPresent()) {
            player.sendMessage(ChatHelper.colored("&c&l✘ &7Zarejstruj się&8: &c/register <hasło> <hasło>"));
            return;
        }
        this.accountService.findByValueOptional(player.getName()).ifPresent(account -> {
            player.sendMessage(ChatHelper.colored("&a&l✔ &7Zalogowano z konta&8: " + (account.isPremium() ? "&a&lPREMIUM" : "&a&lNON-PREMIUM\n&7Zaloguj się&8: &a/login <hasło>")));

            if (!account.isPremium() && account.getState().equals(AccountState.REGISTER)) {
                player.sendMessage(ChatHelper.colored("&c&l✘ &7Zarejstruj się&8: &c/register <hasło> <hasło> " + account.getCaptcha()));
                return;
            }
            if (account.isPremium()) {
                account.setLogin(true);
                return;
            }
            account.setLogin(false);
        });
        new ScheduledThreadPoolExecutor(1).schedule(() -> {
            this.accountService.findByValueOptional(player.getName()).ifPresent(account -> {
                if (account.isPremium() || account.isLogin()) {
                    return;
                }
                player.disconnect(ChatHelper.colored("&cCzas na zalogowanie minał &4&l✘"));
            });
        }, 60L, TimeUnit.SECONDS);
    }
}
