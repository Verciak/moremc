package net.moremc.proxy.auth.plugin.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.moremc.api.API;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.service.entity.AccountService;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;
import net.moremc.proxy.auth.plugin.helper.HashHelper;


public class AccountUnRegisterCommand extends Command
{
    private final AccountService accountService = API.getInstance().getAccountService();

    public AccountUnRegisterCommand(String name) {
        super(name);
    }
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        this.accountService.findByValueOptional(player.getName()).ifPresent(account -> {

            if(account.isPremium()){
                player.disconnect(ChatHelper.colored("&a&l✔ &aTwoje konto pomyślnie zostało odrejstrowane."));
                account.synchronize(SynchronizeType.REMOVE);
                return;
            }

            if(args.length < 1){
                player.sendMessage(ChatHelper.colored("&c&l✘ &7Poprawne użycie&8: &d/unregister <hasło>"));
                return;
            }
            String password = args[0];
            if(!account.isLogin()){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cWystąpił błąd&7, &cnajpierw musisz się zalogować."));
                return;
            }
            if(HashHelper.checkPassword(account, password) && !account.isPremium()){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cWystąpił błąd&7, &chasło nie zgadza się."));
                return;
            }
            player.disconnect(ChatHelper.colored("&a&l✔ &aTwoje konto pomyślnie zostało odrejstrowane."));
            account.synchronize(SynchronizeType.REMOVE);
        });

    }
}
