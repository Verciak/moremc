package net.moremc.master.controller;

import net.moremc.api.API;
import net.moremc.api.data.SectorData;
import net.moremc.api.data.achievement.AchievementConfigurationData;
import net.moremc.api.data.achievement.AchievementData;
import net.moremc.api.data.drop.chest.DropChestData;
import net.moremc.api.data.drop.chest.DropChestDataConfiguration;
import net.moremc.api.data.drop.cobblex.DropCobblexData;
import net.moremc.api.data.drop.cobblex.DropCobblexDataConfiguration;
import net.moremc.api.data.drop.stone.DropStoneData;
import net.moremc.api.data.drop.stone.DropStoneDataConfiguration;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.data.guild.GuildPermissionDataConfiguration;
import net.moremc.api.data.guild.dues.DuesGuildData;
import net.moremc.api.data.guild.dues.DuesGuildDataConfiguration;
import net.moremc.api.data.quest.QuestConfigurationData;
import net.moremc.api.data.quest.QuestData;
import net.moremc.api.entity.server.Server;
import net.moremc.api.sector.Sector;
import net.moremc.master.controller.configuration.SectorDataConfiguration;
import net.moremc.master.controller.runnable.controller.MasterUpdateRunnable;
import net.moremc.master.controller.runnable.sector.SectorSynchronizeRunnable;
import net.moremc.master.controller.runnable.user.UserAutoMessageRunnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MasterServerController
{

    private static MasterServerController instance;
    private SectorData[] sectorData;
    private Map<String, byte[]> schematicDataCompare;
    private static GuildPermissionData permissionData;
    private static DropStoneData dropStoneData;
    private static DropCobblexData dropCobblexData;
    private static DropChestData dropChestData;
    private static AchievementData achievementData;
    private static QuestData questData;
    private static DuesGuildData duesGuildData;

    public static MasterServerController getInstance() {
        return instance;
    }
    public MasterServerController() {
        instance = this;
        this.initializeConfiguration();
        try {
            this.schematicDataCompare = new HashMap<>();
            this.schematicDataCompare.put("obsidian", Files.readAllBytes(Paths.get("obsidian.schematic")));
            this.schematicDataCompare.put("ender_stone", Files.readAllBytes(Paths.get("ender.schematic")));
            this.schematicDataCompare.put("sand", Files.readAllBytes(Paths.get("sand.schematic")));
            this.schematicDataCompare.put("nether", Files.readAllBytes(Paths.get("nether.schematic")));
            this.schematicDataCompare.put("pumpkin", Files.readAllBytes(Paths.get("pumpkin.schematic")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        API api  = new API(true);
        api.initialize(
                "net.moremc.master.controller.nats",
                "moremc_master_controller"
        );
        new MasterUpdateRunnable().start();
        new SectorSynchronizeRunnable().start();
        new UserAutoMessageRunnable().start();

        this.loadSectors();

        if(!api.getServerService().findByValueOptional(1).isPresent()){
            api.getServerService().findOrCreate(1, new Server(1));
        }
    }

        public void initializeConfiguration(){
        SectorDataConfiguration sectorDataConfiguration = new SectorDataConfiguration();
        sectorDataConfiguration.initialize();
        this.sectorData = sectorDataConfiguration.getConfiguration();

        GuildPermissionDataConfiguration permissionDataConfiguration = new GuildPermissionDataConfiguration();
        permissionDataConfiguration.initialize();
        this.permissionData = permissionDataConfiguration.getConfiguration();

        DropStoneDataConfiguration stoneDataConfiguration = new DropStoneDataConfiguration();
        stoneDataConfiguration.initialize();
        this.dropStoneData = stoneDataConfiguration.getConfiguration();

        DropChestDataConfiguration chestDataConfiguration = new DropChestDataConfiguration();
        chestDataConfiguration.initialize();
        this.dropChestData = chestDataConfiguration.getConfiguration();

        DropCobblexDataConfiguration cobblexDataConfiguration = new DropCobblexDataConfiguration();
        cobblexDataConfiguration.initialize();
        this.dropCobblexData = cobblexDataConfiguration.getConfiguration();

        AchievementConfigurationData achievementConfigurationData = new AchievementConfigurationData();
        achievementConfigurationData.initialize();
        this.achievementData = achievementConfigurationData.getConfiguration();

        QuestConfigurationData questConfigurationData = new QuestConfigurationData();
        questConfigurationData.initialize();
        this.questData = questConfigurationData.getConfiguration();

        DuesGuildDataConfiguration duesGuildDataConfiguration = new DuesGuildDataConfiguration();
        duesGuildDataConfiguration.initialize();
        this.duesGuildData = duesGuildDataConfiguration.getConfiguration();
    }
    public void loadSectors(){
        for (SectorData sectorDatum : this.sectorData) {
            API.getInstance().getSectorService().getMap().put(sectorDatum.getName(), new Sector(sectorDatum.getName(), sectorDatum.getType(), sectorDatum.getLocationInfo()));
        }
    }

    public DuesGuildData getDuesGuildData() {
        return duesGuildData;
    }

    public QuestData getQuestData() {
        return questData;
    }

    public AchievementData getAchievementData() {
        return achievementData;
    }

    public GuildPermissionData getPermissionData() {
        return permissionData;
    }

    public Map<String, byte[]> getSchematicDataCompare() {
        return schematicDataCompare;
    }

    public SectorData[] getSectorData() {
        return sectorData;
    }

    public DropCobblexData getDropCobblexData() {
        return dropCobblexData;
    }

    public DropStoneData getDropStoneData() {
        return dropStoneData;
    }

    public DropChestData getDropChestData() {
        return dropChestData;
    }
}
