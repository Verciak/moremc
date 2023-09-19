package net.moremc.master.controller.nats.client;

import net.moremc.api.API;
import net.moremc.api.data.achievement.AchievementData;
import net.moremc.api.data.drop.chest.DropChestData;
import net.moremc.api.data.drop.cobblex.DropCobblexData;
import net.moremc.api.data.drop.stone.DropStoneData;
import net.moremc.api.data.guild.GuildPermissionData;
import net.moremc.api.data.guild.dues.DuesGuildData;
import net.moremc.api.data.quest.QuestData;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.configuration.request.ConfigurationLoadRequestPacket;
import net.moremc.api.nats.packet.configuration.response.ConfigurationLoadResponseClientPacket;
import net.moremc.master.controller.MasterServerController;

public class ConfigurationLoadRequestHandler extends PacketMessengerHandler<ConfigurationLoadRequestPacket> {

    private final GuildPermissionData permissionData = MasterServerController.getInstance().getPermissionData();
    private final DropStoneData dropStoneData = MasterServerController.getInstance().getDropStoneData();
    private final DropCobblexData dropCobblexData = MasterServerController.getInstance().getDropCobblexData();
    private final DropChestData dropChestData = MasterServerController.getInstance().getDropChestData();
    private final AchievementData achievementData = MasterServerController.getInstance().getAchievementData();
    private final QuestData questData = MasterServerController.getInstance().getQuestData();
    private final DuesGuildData duesGuildData = MasterServerController.getInstance().getDuesGuildData();

    public ConfigurationLoadRequestHandler() {
        super(ConfigurationLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(ConfigurationLoadRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getSectorSender(), new ConfigurationLoadResponseClientPacket(this.permissionData,
                this.dropStoneData, this.dropCobblexData, this.dropChestData,
                this.achievementData,
                this.questData,
                this.duesGuildData));

    }
}
