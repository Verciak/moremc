package net.moremc.guilds.command.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuildSubCommand extends Command {


    private final String name;

    public GuildSubCommand(String name, String... aliases) {
        super(name,"", "", Arrays.asList(aliases));
        this.name = name;

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        return onCommand(player, args);
    }
    public abstract boolean onCommand(Player player, String[] args);

    @Override
    public List<String> tabComplete(CommandSender sender,  String alias,  String[] args) throws IllegalArgumentException {
        if(this.tabComplete(sender, alias, args) == null)return new ArrayList<>();
        return this.tabComplete(sender, alias, args);
    }
}
