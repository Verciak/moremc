package net.moremc.guilds.protocol;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.moremc.guilds.service.TeamService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.helper.RandomStringHelper;
import net.moremc.api.service.entity.UserService;
import net.moremc.guilds.GuildsPlugin;

import java.util.*;

public class IncognitoProtocolHandler extends PacketAdapter {

    private final TeamService teamService = GuildsPlugin.getInstance().getTeamService();
    private final UserService userService = API.getInstance().getUserService();

    public IncognitoProtocolHandler() {
        super(GuildsPlugin.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.PLAYER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().getValues().get(0);

        if (!event.getPacket().getPlayerInfoAction().getValues().get(0).equals(EnumWrappers.PlayerInfoAction.ADD_PLAYER)) {
            return;
        }

        Set<PlayerInfoData> newPlayerInfoDataList = new HashSet<>();
        for (PlayerInfoData playerInfoData : playerInfoDataList) {

            if (!API.getInstance().getUserService().findUserByUUID(playerInfoData.getProfile().getUUID()).isPresent()) {
                newPlayerInfoDataList.add(playerInfoData);
                continue;
            }

            this.userService.findUserByUUID(playerInfoData.getProfile().getUUID()).ifPresent(user -> {

                if (!user.isIncognito()) {
                    newPlayerInfoDataList.add(playerInfoData);
                    return;
                }
                WrappedGameProfile profile = playerInfoData.getProfile();
                String newNick = user.getFakeNickname();
                if(user.getFakeNickname().equalsIgnoreCase(user.getNickName())){
                     newNick = new RandomStringHelper(5, new Random(), user.getNickName()).nextString();
                }

                WrappedGameProfile newProfile = profile.withName(newNick);
                newProfile.getProperties().putAll(profile.getProperties());

                PlayerInfoData newPlayerInfoData = new PlayerInfoData(newProfile, playerInfoData.getPing(), playerInfoData.getGameMode(), playerInfoData.getDisplayName());
                newPlayerInfoDataList.add(newPlayerInfoData);

                user.setFakeNickname(newNick);

                Player player = Bukkit.getPlayer(user.getUuid());
                if (player != null) {
                    this.teamService.removeNameTag(player, user);
                    this.teamService.initializeNameTag(player, user);
                }
            });
        }
        List<PlayerInfoData> playerInfoDataLists = new ArrayList<>(newPlayerInfoDataList);
        event.getPacket().getPlayerInfoDataLists().write(0, playerInfoDataLists);
    }
}