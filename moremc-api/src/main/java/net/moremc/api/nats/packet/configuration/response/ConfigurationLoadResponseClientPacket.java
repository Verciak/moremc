package net.moremc.api.nats.packet.configuration.response;

import net.moremc.api.data.achievement.AchievementData;
import net.moremc.api.data.drop.chest.DropChestData;
import net.moremc.api.data.drop.cobblex.DropCobblexData;
import net.moremc.api.data.drop.stone.DropStoneData;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.data.guild.dues.DuesGuildData;
import net.moremc.api.data.quest.QuestData;
import net.moremc.api.nats.packet.server.ResponsePacket;

public class ConfigurationLoadResponseClientPacket extends ResponsePacket {

    private final GuildPermissionData permissionData;
    private final DropStoneData dropStoneData;
    private final DropCobblexData dropCobblexData;
    private final DropChestData dropChestData;
    private final AchievementData achievementData;
    private final QuestData questData;
    private final DuesGuildData duesGuildData;

    public ConfigurationLoadResponseClientPacket(GuildPermissionData permissionData, DropStoneData dropStoneData, DropCobblexData dropCobblexData, DropChestData dropChestData,
                                                 AchievementData achievementData, QuestData questData, DuesGuildData duesGuildData){
        this.permissionData = permissionData;
        this.dropStoneData = dropStoneData;
        this.dropCobblexData = dropCobblexData;
        this.dropChestData = dropChestData;
        this.achievementData = achievementData;
        this.questData = questData;
        this.duesGuildData = duesGuildData;
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

    public DropChestData getDropChestData() {
        return dropChestData;
    }

    public DropStoneData getDropStoneData() {
        return dropStoneData;
    }

    public DropCobblexData getDropCobblexData() {
        return dropCobblexData;
    }

    public GuildPermissionData getPermissionData() {
        return permissionData;
    }
}
