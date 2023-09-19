package net.moremc.proxy.auth.plugin.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.moremc.api.API;
import net.moremc.api.entity.account.state.AccountState;
import net.moremc.api.service.entity.AccountService;
import net.moremc.proxy.auth.plugin.helper.ChatHelper;
import net.moremc.proxy.auth.plugin.helper.HashHelper;

public class AccountRegisterCommand extends Command
{
    private final AccountService accountService = API.getInstance().getAccountService();

    public AccountRegisterCommand(String name) {
        super(name);
    }
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if(args.length < 3){
            player.sendMessage(ChatHelper.colored("&c&l✘ &7Poprawne użycie&8: &d/register <hasło> <hasło> <captcha>"));
            return;
        }
        String passwordOne = args[0];
        String passwordTwo = args[1];
        String captcha  = args[2];

        this.accountService.findByValueOptional(player.getName()).ifPresent(account -> {

            if(account.isPremium()){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cGracz &4PREMIUM &cnie muszą się rejstrować."));
                return;
            }
            if(account.getState().equals(AccountState.LOGIN)){
                player.sendMessage(ChatHelper.colored("&c&l✘ &4Wystąpił błąd&7, &cjesteś już zarejstrowany."));
                return;
            }
            if(passwordOne.length() <= 4){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cHasło musi mieć przynajmniej 4 znaki."));
                return;
            }
            if(!passwordOne.equalsIgnoreCase(passwordTwo)){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cHasła nie zgadzają się."));
                return;
            }
            if(!captcha.equalsIgnoreCase(account.getCaptcha())){
                player.sendMessage(ChatHelper.colored("&c&l✘ &cPodałeś złego captcha twój kod captcha&8: &4" + account.getCaptcha()));
                return;
            }
            player.sendMessage(ChatHelper.colored("&a&l✔ &aRejstracja twojego konta przebiegła pomyślnie."));
            account.setLogin(true);
            account.setState(AccountState.LOGIN);
            account.setPassword(HashHelper.hashPassword(passwordOne));
        });
    }

}
