package net.moremc.proxy.auth.plugin.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.moremc.api.API;
import net.moremc.api.entity.account.state.AccountState;
import net.moremc.api.service.entity.AccountService;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;
import net.moremc.proxy.auth.plugin.helper.HashHelper;

public class AccountLoginCommand extends Command
{

    private final AccountService accountService = API.getInstance().getAccountService();

    public AccountLoginCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;


        this.accountService.findByValueOptional(player.getName()).ifPresent(account -> {

            if(args.length < 1){
                player.sendMessage(ChatHelper.colored("&c&l✘ &7Poprawne użycie&8: &d/login <hasło>"));
                return;
            }

            if(account.isPremium()){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cGracz &4PREMIUM &cnie muszą się logować."));
                return;
            }
            if(account.getState().equals(AccountState.REGISTER)){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cWystąpił bład&7, &cnajpierw musisz się zarejstrować&8: &4/register"));
                return;
            }
            if(account.isLogin()){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cWystąpił błąd&7, &cjesteś już zalogowany."));
                return;
            }

            String password = args[0];
            if(HashHelper.checkPassword(account, password)){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cWystąpił błąd&7, &cpodałeś złe hasło."));
                return;
            }
            account.setLogin(true);
            player.sendMessage(ChatHelper.colored("&a&l✔ &aLogowanie twojego konta przebiegło pomyślnie."));
        });

    }
}
