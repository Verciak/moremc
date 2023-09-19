package net.moremc.bukkit.tools.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.moremc.api.API;
import net.moremc.api.data.quest.Quest;
import net.moremc.api.data.quest.QuestData;
import net.moremc.api.entity.user.User;
import net.moremc.api.entity.user.UserQuest;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import org.bukkit.entity.Player;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestScoreboard implements AssembleAdapter
{
    private final UserService userService = API.getInstance().getUserService();
    private final QuestData questData = CommunicatorPlugin.getInstance().getQuestData();

    @Override
    public String getTitle(Player player) {
        return MessageHelper.colored("&5&m-> &8 &d&lPAY&f&lMC &5&m <-");
    }
    @Override
    public List<String> getLines(Player player) {
        User user = userService.findByValue(player.getName());
        UserQuest userQuest = user.findUserQuestActive();

        if(userQuest == null)return new ArrayList<>();

        Quest quest = questData.findQuestById(userQuest.getId());

        String status = MessageHelper.progress((int) ((userQuest.getAmountProgress() * 1.0) / quest.getProgress()), (int) (10 - ((userQuest.getAmountProgress() * 1.0) / quest.getProgress())));
        double progressLine = ((userQuest.getAmountProgress() * 1.0) / quest.getProgressLine()) * 100;

        return MessageHelper.translateText(
                Arrays.asList(
                        " ",
                        "&fJesteś podczas zadania:",
                        "              &5#&d" + quest.getId(),
                        "   ",
                        "&fCzas&8: &d" + DataHelper.getTimeToString(userQuest.getActiveTime()),
                        "&fPostęp&8(&d" + userQuest.getAmountProgress() + "&8/&5" + quest.getAmountRequired() + "&8)",
                        "&8(" + status + "&7, &d" + new DecimalFormat("##.##").format(progressLine) + "%&8)",
                        "         ",
                        "&fWięcej informacji pod:",
                        "        &d/zadania",
                        "              ",
                        "       &7p a y m c . p l"));
    }
}