package net.moremc.guilds;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import net.moremc.bukkit.api.helper.ReflectionHelper;
import net.moremc.guilds.command.GuildCommand;
import net.moremc.guilds.generator.GuildGeneratorBukkitCache;
import net.moremc.guilds.listeners.entity.EntityDamageByEntityDamageEventHandler;
import net.moremc.guilds.listeners.entity.EntityExplodeEventHandler;
import net.moremc.guilds.listeners.other.SignEditorEventHandler;
import net.moremc.guilds.listeners.player.*;
import net.moremc.guilds.listeners.region.GuildRegionBlockBreakEventHandler;
import net.moremc.guilds.listeners.region.GuildRegionBlockPlaceEventHandler;
import net.moremc.guilds.listeners.region.GuildRegionPlayerMoveEventHandler;
import net.moremc.guilds.protocol.IncognitoProtocolHandler;
import net.moremc.guilds.runnable.*;
import net.moremc.guilds.scoreboard.GuildGeneratorScoreboard;
import net.moremc.guilds.scoreboard.GuildRegenerationScoreboard;
import net.moremc.guilds.service.CustomItemService;
import net.moremc.guilds.service.TeamService;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GuildsPlugin extends JavaPlugin {

    private static GuildsPlugin instance;
    private TeamService teamService;
    private CustomItemService customItemService;
    private GuildGeneratorBukkitCache generatorBukkitCache;

    public static GuildsPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.customItemService = new CustomItemService();
        this.customItemService.load();
        this.teamService = new TeamService();

        new TerrainViewPlayerRunnable().start();
        new GuildAreaSynchronizeRunnable().start();
        new NameTagUpdateRunnable().start();
        new ArmorStandUpdateRunnable().start();
        new GuildPlayerPeriscopeUpdateRunnable().start();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerGuildInteractEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityDamageEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new SignEditorEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerToggleShiftEventHandler(), this);


        Bukkit.getPluginManager().registerEvents(new GuildRegionBlockBreakEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new GuildRegionBlockPlaceEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new GuildRegionPlayerMoveEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new EntityExplodeEventHandler(), this);


        this.loadGuilds(new GuildCommand());

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new IncognitoProtocolHandler());

        this.registerScoreboard();

        this.generatorBukkitCache = new GuildGeneratorBukkitCache();
    }
    private void registerScoreboard() {
        Assemble assemble = new Assemble(this, new GuildGeneratorScoreboard());
        assemble.setAssembleStyle(AssembleStyle.KOHI);
        assemble.setTicks(1L);

        assemble = new Assemble(this, new GuildRegenerationScoreboard());
        assemble.setAssembleStyle(AssembleStyle.KOHI);
        assemble.setTicks(1L);

    }
    public void loadGuilds(org.bukkit.command.Command command){
        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) ReflectionHelper.getField(SimplePluginManager.class, "commandMap", SimpleCommandMap.class).get(Bukkit.getPluginManager());
            simpleCommandMap.register(command.getName(), command);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded guild commands");
    }

    public CustomItemService getCustomItemService() {
        return customItemService;
    }

    public GuildGeneratorBukkitCache getGeneratorBukkitCache() {
        return generatorBukkitCache;
    }

    public TeamService getTeamService() {
        return teamService;
    }
}
