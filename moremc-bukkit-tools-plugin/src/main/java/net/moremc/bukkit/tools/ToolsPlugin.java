package net.moremc.bukkit.tools;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import me.vaperion.blade.Blade;
import me.vaperion.blade.bindings.impl.BukkitBindings;
import me.vaperion.blade.container.impl.BukkitCommandContainer;
import net.moremc.api.API;
import net.moremc.api.sector.Sector;
import net.moremc.bukkit.tools.commands.admin.*;
import net.moremc.bukkit.tools.commands.player.*;
import net.moremc.bukkit.tools.listeners.block.BlockBreakDropEventHandler;
import net.moremc.bukkit.tools.listeners.block.BlockFromEventHandler;
import net.moremc.bukkit.tools.listeners.block.BlockPlaceDropEventHandler;
import net.moremc.bukkit.tools.listeners.crafting.CraftingGameplayBlockPlaceEventHandler;
import net.moremc.bukkit.tools.listeners.other.SignEditorEventHandler;
import net.moremc.bukkit.tools.listeners.player.*;
import net.moremc.bukkit.tools.listeners.region.SpawnRegionInteractionsEventHandler;
import net.moremc.bukkit.tools.runnable.other.TablistUpdateRunnable;
import net.moremc.bukkit.tools.runnable.player.PlayerAntiLogoutRunnable;
import net.moremc.bukkit.tools.runnable.player.PlayerDepositRunnable;
import net.moremc.bukkit.tools.runnable.player.PlayerProtectionRunnable;
import net.moremc.bukkit.tools.scoreboard.QuestScoreboard;
import net.moremc.bukkit.tools.scoreboard.SpawnScoreboard;
import net.moremc.bukkit.tools.service.CraftingService;
import net.moremc.bukkit.tools.service.CustomItemService;
import net.moremc.bukkit.tools.service.ItemShopService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class ToolsPlugin extends JavaPlugin
{
    private static ToolsPlugin instance;

    private CraftingService craftingService;
    private CustomItemService customItemService;
    private ItemShopService itemShopService;


    @Override
    public void onEnable() {
        instance = this;
        register();
    }

    @Override
    public void onDisable() {

    }
    private void register() {
        registerService();
        registerCommand();
        registerRunnables();
        registerScoreboard();
        registerListeners();
    }
    private void registerService() {
        customItemService = new CustomItemService();
        customItemService.load();

        craftingService = new CraftingService();
        craftingService.load();

        itemShopService = new ItemShopService();
        itemShopService.load();
    }
    private void registerCommand() {
        Blade blade = Blade.of().fallbackPrefix("moremc").overrideCommands(true)
                .executionTimeWarningThreshold(1500L)
                .containerCreator(BukkitCommandContainer.CREATOR).binding(new BukkitBindings())
                .asyncExecutor(runnable -> getServer().getScheduler().runTaskAsynchronously(ToolsPlugin.this, runnable)).build();

        blade.register(new AdminChatCommand(ToolsPlugin.this));
        blade.register(new AdminItemsCommand(ToolsPlugin.this, this.customItemService));
        blade.register(new AdminItemShopCommand(ToolsPlugin.this, itemShopService, customItemService));
        blade.register(new AdminPanelCommand(ToolsPlugin.this));
        blade.register(new AdminTeleportCommand(ToolsPlugin.this));
        blade.register(new BackupCommand(ToolsPlugin.this));
        blade.register(new BanCommand(ToolsPlugin.this));
        blade.register(new BroadcastCommand(ToolsPlugin.this));
        blade.register(new CaseCommand(ToolsPlugin.this));
        blade.register(new ChatCommand(ToolsPlugin.this));
        blade.register(new FlyCommand(ToolsPlugin.this));
        blade.register(new GamemodeCommand());
        blade.register(new GodCommand(ToolsPlugin.this));
        blade.register(new HealCommand(ToolsPlugin.this));
        blade.register(new KickCommand(ToolsPlugin.this));
        blade.register(new ListCommand(ToolsPlugin.this));
        blade.register(new MuteCommand(ToolsPlugin.this));
        blade.register(new SectorsCommand(ToolsPlugin.this));
        blade.register(new SpeedCommand(ToolsPlugin.this));
        blade.register(new VanishCommand(ToolsPlugin.this));
        blade.register(new AdminKitConfigurationCommand(ToolsPlugin.this));

        blade.register(new AchievementCommand(ToolsPlugin.this));
        blade.register(new AwardCommand(ToolsPlugin.this));
        blade.register(new BackPackDescriptionCommand(ToolsPlugin.this));
        blade.register(new ChannelCommand(ToolsPlugin.this));
        blade.register(new CobblexCommand(ToolsPlugin.this, this.customItemService));
        blade.register(new CraftingCommand(ToolsPlugin.this, craftingService));
        blade.register(new DepositCommand(ToolsPlugin.this));
        blade.register(new EnderChestCommand(ToolsPlugin.this));
        blade.register(new HelpCommand(ToolsPlugin.this));
        blade.register(new HelpOPCommand(ToolsPlugin.this));
        blade.register(new HomeCommand(ToolsPlugin.this));
        blade.register(new IncognitoCommand(ToolsPlugin.this));
        blade.register(new ItemShopCommand(ToolsPlugin.this));
        blade.register(new KitCommand(ToolsPlugin.this));
        blade.register(new KickCommand(ToolsPlugin.this));
        blade.register(new MessageCommand(ToolsPlugin.this));
        blade.register(new PlayerCommand(ToolsPlugin.this));
        blade.register(new ProtectionCommand(ToolsPlugin.this));
        blade.register(new RepairCommand(ToolsPlugin.this));
        blade.register(new SpawnCommand(ToolsPlugin.this));
        blade.register(new SVIPCommand(ToolsPlugin.this));
        blade.register(new TeleportCommand(ToolsPlugin.this));
        blade.register(new TrashCommand(ToolsPlugin.this));
        blade.register(new VIPCommand(ToolsPlugin.this));
        blade.register(new WorkBenchCommand(ToolsPlugin.this));
        blade.register(new DiscordCommand(ToolsPlugin.this));
        blade.register(new DropCommand(ToolsPlugin.this));
        blade.register(new GroupCommand(ToolsPlugin.this));
        blade.register(new ChatSettingCommand(ToolsPlugin.this));
        blade.register(new QuestCommand(ToolsPlugin.this));
        blade.register(new BazaarCommand(ToolsPlugin.this));
        blade.register(new WhitelistCommand(ToolsPlugin.this));

    }
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new SignEditorEventHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEventHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakDropEventHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceDropEventHandler(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuestEventHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockFromEventHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerUseRandomTeleportEventHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEventHandler(), this);
        getServer().getPluginManager().registerEvents(new SpawnRegionInteractionsEventHandler(), this);
        getServer().getPluginManager().registerEvents(new CraftingGameplayBlockPlaceEventHandler(), this);
    }
    private void registerRunnables() {
        new TablistUpdateRunnable().start();
        new PlayerDepositRunnable().start();
        new PlayerAntiLogoutRunnable().start();
        new PlayerProtectionRunnable().start();
    }
    private void registerScoreboard() {
        Optional<Sector> currentSector = API.getInstance().getSectorService().getCurrentSector();
        currentSector.ifPresent(consumer -> {
            if(!consumer.isSpawn()){
                Assemble assemble = new Assemble(this, new QuestScoreboard());
                assemble.setAssembleStyle(AssembleStyle.KOHI);
                assemble.setTicks(1L);
            }
            if (consumer.isSpawn()) {
                Assemble assemble = new Assemble(this, new SpawnScoreboard());
                assemble.setAssembleStyle(AssembleStyle.KOHI);
                assemble.setTicks(1L);
            }
        });
    }

    public CustomItemService getCustomItemService() {
        return customItemService;
    }

    public CraftingService getCraftingService() {
        return craftingService;
    }

    public static ToolsPlugin getInstance() {
        return instance;
    }
}
