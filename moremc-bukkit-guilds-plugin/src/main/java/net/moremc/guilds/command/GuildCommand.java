package net.moremc.guilds.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.guilds.command.sub.GuildSubCommand;

import java.util.*;

public class GuildCommand extends GuildSubCommand {


    private final Set<GuildSubCommand> subCommands = new HashSet<>();

    public GuildCommand() {
        super("gildia",  "g", "gildie", "guild");
        this.subCommands.add(new CreateGuildCommand());
        this.subCommands.add(new DeleteGuildCommand());
        this.subCommands.add(new ConfigurationGuildCommand());
        this.subCommands.add(new InviteGuildCommand());
        this.subCommands.add(new HomeGuildCommand());
        this.subCommands.add(new PanelGuildCommand());
        this.subCommands.add(new JoinGuildCommand());
        this.subCommands.add(new LeaveGuildCommand());
        this.subCommands.add(new KickPlayerGuildCommand());
        this.subCommands.add(new SetLocationHomeGuildCommand());
        this.subCommands.add(new SetMasterGuildCommand());
        this.subCommands.add(new SetLeaderGuildCommand());
        this.subCommands.add(new SetOwnerGuildCommand());
        this.subCommands.add(new FriendlyFireGuildCommand());
        this.subCommands.add(new DuesGuildCommand());
        this.subCommands.add(new TreasureGuildCommand());
        this.subCommands.add(new InfoGuildCommand());
        this.subCommands.add(new TakeSelectionAreaGuildCommand());
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        List<String> commandUsageMessage = Arrays.asList(
                        "&f«&d&l&m----[&7&l&m---&f&m&l[--&f &d&lGILDIE &f&l&m--]&7&l&m---]&d&l&m----&8&f»",
                        "&8>> &d/gildia zaloz <nazwa> <pelnaNazwa> &8- &fZakladanie gildii",
                        "&8>> &d/gildia itemy &8- &fZobacz przedmioty na gildie",
                        "&8>> &d/gildia zapros <nick> &8- &fZapraszanie gracza do gildii",
                        "&8>> &d/gildia wyrzuc <nick> &8- &fUsuwanie gracza z gildii",
                        "&8>> &d/gildia dolacz &8- &fDolacz do gildii",
                        "&8>> &d/gildia opusc &8- &fOpusc wlasna gildie",
                        "&8>> &d/gildia info <tag> &8- &fInformacje o danej gildii",
                        "&8>> &d/gildia sojusz &8- &fZarzadzaj sojuszami swojej gildii",
                        "&8>> &d/gildia apvp &8- &fZarzadzanie pvp w sojuszu",
                        "&8>> &d/gildia usun &8- &fUsuwanie swojej gildii",
                        "&8>> &d/gildia skarbiec &8- &fSkarbiec gildyjny",
                        "&8>> &d/gildia ustawdom &8- &fUstaw lokalizacje teleportu",
                        "&8>> &d/gildia baza &8- &fTeleportacja do bazy",
                        "&8>> &d/gildia zalozyciel <nick> &8- &fNadaj nowego zalozyciela",
                        "&8>> &d/gildia zastepca <nick> &8- &fNadaj nowego zastepce",
                        "&8>> &d/gildia mistrz <nick> &8- &fNadaj nowego mistrza",
                        "&8>> &d/gildia panel &8- &fZarzadzaj ustawieniami gildyjnymi",
                        "&f«&d&l&m----[&7&l&m---&f&m&l[--&f&l&m&8&f»     «&l&m--]&7&l&m---]&d&l&m----&8&f»");

        if(args.length == 0){
            commandUsageMessage.forEach(message -> player.sendMessage(MessageHelper.colored(message)));
            return false;
        }

        String name = args[0];

        GuildSubCommand subCommand = this.getSubCommand(name);
        if(subCommand == null){
            commandUsageMessage.forEach(message -> player.sendMessage(MessageHelper.colored(message)));
            return false;
        }
        return subCommand.onCommand(player, Arrays.copyOfRange(args, 1, args.length));
    }
    private GuildSubCommand getSubCommand(String sub) {
        for (GuildSubCommand sc : subCommands)
            if (sc.getName().equalsIgnoreCase(sub) || sc.getAliases().contains(sub))
                return sc;
        return null;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
    }
}
