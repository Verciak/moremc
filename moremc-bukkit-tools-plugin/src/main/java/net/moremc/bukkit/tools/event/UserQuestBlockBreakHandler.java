package net.moremc.bukkit.tools.event;

import net.moremc.api.API;
import net.moremc.api.entity.user.UserQuest;
import net.moremc.api.entity.user.type.UserQuestType;
import net.moremc.api.entity.user.type.UserSettingMessageType;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.service.entity.UserService;
import net.moremc.sectors.event.UserBlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class UserQuestBlockBreakHandler implements Consumer<UserBlockBreakEvent> {


    private final UserService userService = API.getInstance().getUserService();

    @Override
    public void accept(UserBlockBreakEvent eventBlockBreak) {
        eventBlockBreak.getOptionalUser().ifPresent(user -> {
            UserQuest userQuest = user.findUserQuestActive();
            if(userQuest == null)return;

            Player player = eventBlockBreak.getBlockBreakEvent().getPlayer();
            Block block = eventBlockBreak.getBlockBreakEvent().getBlock();

            if(userQuest.getUserQuestType().equals(UserQuestType.BREAK_OAK)){
                if(block.getType().equals(Material.LOG)) {
                    userQuest.setAmountProgress(userQuest.getAmountProgress() + 1);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);

                    if (userQuest.getAmountProgress() >= userQuest.getAmountRequired()) {
                        userQuest.setDone(true);

                        ClientSendMessagePacket packet = new ClientSendMessagePacket("&5&lZadania &8>> &fGracz&8: &d" + player.getName() + " &fzakończył swoje zadanie&8: &8(&5#&d" + userQuest.getId() +
                                "&7, &d" + DataHelper.getTimeToString(userQuest.getActiveTime()) + "&8)",
                                SendMessageReceiverType.PLAYER, SendMessageType.CHAT);

                        this.userService.findByValueOptional(player.getName()).filter(userFind -> userFind.findUserSettingByType(UserSettingMessageType.QUEST).isStatus()).ifPresent(userFind -> {
                            packet.setNickNameTarget(userFind.getNickName());
                            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", packet);
                        });
                        userQuest.setActiveTime(0);
                    }
                }
            }
        });
    }
}
