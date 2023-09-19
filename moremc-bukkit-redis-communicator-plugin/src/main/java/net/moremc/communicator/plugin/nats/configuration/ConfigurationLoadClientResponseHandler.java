package net.moremc.communicator.plugin.nats.configuration;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.configuration.response.ConfigurationLoadResponseClientPacket;
import net.moremc.communicator.plugin.CommunicatorPlugin;

public class ConfigurationLoadClientResponseHandler extends PacketMessengerHandler<ConfigurationLoadResponseClientPacket> {


    public ConfigurationLoadClientResponseHandler() {
        super(ConfigurationLoadResponseClientPacket.class, API.getInstance().getSectorName());
    }

    @Override
    public void onHandle(ConfigurationLoadResponseClientPacket packet) {
        CommunicatorPlugin.getInstance().setPermissionData(packet.getPermissionData());
        CommunicatorPlugin.getInstance().setDropChestData(packet.getDropChestData());
        CommunicatorPlugin.getInstance().setDropCobblexData(packet.getDropCobblexData());
        CommunicatorPlugin.getInstance().setDropStoneData(packet.getDropStoneData());
        CommunicatorPlugin.getInstance().setAchievementData(packet.getAchievementData());
        CommunicatorPlugin.getInstance().setQuestData(packet.getQuestData());
        CommunicatorPlugin.getInstance().setDuesGuildData(packet.getDuesGuildData());
    }
}
