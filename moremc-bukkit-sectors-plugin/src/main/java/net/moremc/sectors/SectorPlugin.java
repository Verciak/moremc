package net.moremc.sectors;

import net.moremc.api.API;
import net.moremc.api.nats.packet.sector.request.SectorLoadDataRequestPacket;
import net.moremc.bukkit.api.data.ApiConfig;
import net.moremc.communicator.plugin.CommunicatorPlugin;
import net.moremc.sectors.handler.PlayerMoveEventHandler;
import net.moremc.sectors.handler.PlayerTeleportEventHandler;
import net.moremc.sectors.handler.scenario.*;
import net.moremc.sectors.runnable.NearSectorInformationRunnable;
import net.moremc.sectors.runnable.SectorSynchronizeRunnable;
import net.moremc.sectors.runnable.TimeWorldSynchronizeRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class SectorPlugin extends JavaPlugin implements PluginMessageListener
{

    private static SectorPlugin instance;
    private String sectorName;

    public static SectorPlugin getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        ApiConfig apiConfig = new ApiConfig(this, "config");
        apiConfig.load();
        apiConfig.setInstantSave(true);

        this.sectorName = this.getConfig().getString("name");
        API.getInstance().setSectorName(this.getConfig().getString("name"));
        CommunicatorPlugin.getInstance().initialize(API.getInstance(), this.getConfig().getString("name"));

        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new SectorLoadDataRequestPacket(this.sectorName));

        this.registerEventHandler();

        new NearSectorInformationRunnable().start();
        new SectorSynchronizeRunnable().start();
        new TimeWorldSynchronizeRunnable().start();
    }

    private void registerEventHandler() {
        Bukkit.getPluginManager().registerEvents(new PlayerMoveEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleportEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakPlaceScenarioEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageScenarioEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryScenarioEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerBucketEmptyFillEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractScenarioEventHandler(), this);
    }

    public String getSectorName() {
        return sectorName;
    }
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }
}
