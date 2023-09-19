package net.moremc.communicator.plugin;

import net.moremc.api.API;
import net.moremc.api.data.achievement.AchievementData;
import net.moremc.api.data.drop.chest.DropChestData;
import net.moremc.api.data.drop.cobblex.DropCobblexData;
import net.moremc.api.data.drop.stone.DropStoneData;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.data.guild.dues.DuesGuildData;
import net.moremc.api.data.quest.QuestData;
import net.moremc.api.nats.packet.ban.load.BanInformationLoadRequestPacket;
import net.moremc.api.nats.packet.bazaar.load.BazaarLoadRequestPacket;
import net.moremc.api.nats.packet.client.request.SchematicByteDataRequestPacket;
import net.moremc.api.nats.packet.configuration.request.ConfigurationLoadRequestPacket;
import net.moremc.api.nats.packet.guild.load.GuildLoadRequestPacket;
import net.moremc.api.nats.packet.kit.request.KitLoadRequestPacket;
import net.moremc.api.nats.packet.user.load.UserLoadRequestPacket;
import net.moremc.api.nats.packet.whitelist.load.ServerWhitelistLoadRequestPacket;
import org.bukkit.plugin.java.JavaPlugin;

public class CommunicatorPlugin extends JavaPlugin {


    private static CommunicatorPlugin instance;
    private String sectorName;
    private GuildPermissionData permissionData;
    private DropStoneData dropStoneData;
    private DropChestData dropChestData;
    private DropCobblexData dropCobblexData;
    private AchievementData achievementData;
    private QuestData questData;
    private DuesGuildData duesGuildData;


    public static CommunicatorPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new API(false);
    }

    @Override
    public void onDisable() {

    }
    public void initialize(API api, String sectorName) {
        this.sectorName = sectorName;

        api.initialize("net.moremc.communicator.plugin.nats", "moremc_sectors", sectorName,
                "moremc_users_channel",
                "moremc_guilds_channel",
                "moremc_global_channel",
                "moremc_client_channel",
                "moremc_bazaars_channel",
                "moremc_kits_channel",
                "moremc_server_channel",
                "moremc_ban_channel"
        );

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new SchematicByteDataRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new ConfigurationLoadRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new KitLoadRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new UserLoadRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new GuildLoadRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new BazaarLoadRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new BanInformationLoadRequestPacket(sectorName));

        api.getNatsMessengerAPI().sendPacket("moremc_master_controller",
                new ServerWhitelistLoadRequestPacket(sectorName));
    }

    public QuestData getQuestData() {
        return questData;
    }

    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    public GuildPermissionData getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(GuildPermissionData permissionData) {
        this.permissionData = permissionData;
    }

    public String getSectorName() {
        return sectorName;
    }

    public DropStoneData getDropStoneData() {
        return dropStoneData;
    }

    public void setDropStoneData(DropStoneData dropStoneData) {
        this.dropStoneData = dropStoneData;
    }

    public DropChestData getDropChestData() {
        return dropChestData;
    }

    public void setDropChestData(DropChestData dropChestData) {
        this.dropChestData = dropChestData;
    }

    public DropCobblexData getDropCobblexData() {
        return dropCobblexData;
    }

    public void setDropCobblexData(DropCobblexData dropCobblexData) {
        this.dropCobblexData = dropCobblexData;
    }

    public AchievementData getAchievementData() {
        return achievementData;
    }

    public void setAchievementData(AchievementData achievementData) {
        this.achievementData = achievementData;
    }

    public DuesGuildData getDuesGuildData() {
        return duesGuildData;
    }

    public void setDuesGuildData(DuesGuildData duesGuildData) {
        this.duesGuildData = duesGuildData;
    }
}
