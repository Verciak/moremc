package net.moremc.bukkit.tools.inventories.other;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.data.quest.Quest;
import net.moremc.api.data.quest.QuestData;
import net.moremc.api.entity.user.UserQuest;
import net.moremc.api.helper.DataHelper;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class QuestInventory extends InventoryHelperImpl {


    private final QuestData questData = CommunicatorPlugin.getInstance().getQuestData();
    private final UserService userService = API.getInstance().getUserService();

    public QuestInventory() {
        super("&dZadania serwerowa", 27);
    }


    public void init(Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(this.questData == null){
                player.sendMessage(MessageHelper.colored("&4Błąd: &cPodczas konfiguracji zadań zgłoś to admininstracji!!!"));
                return;
            }
            for (Quest quest : this.questData.getQuests()) {
                if(quest == null){
                    player.sendMessage(MessageHelper.colored("&4Błąd: &cPodczas konfiguracji zadań zgłoś to admininstracji!!"));
                    continue;
                }
                user.addQuest(quest);
            }
        });
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        this.init(player);

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {


            Integer[] glassYellowSlots = new Integer[]{1, 3, 5, 7, 9, 17, 20, 24};
            Integer[] glassBlackSlots = new Integer[]{2, 4, 6, 19, 21, 23, 25};
            Integer[] glassWhiteSlots = new Integer[]{0, 8, 18, 26, 22};

            Arrays.stream(glassYellowSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
            Arrays.stream(glassBlackSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack()));
            Arrays.stream(glassWhiteSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 0).setName(" ").toItemStack()));


            for (Quest quest : this.questData.getQuests()) {
                UserQuest userQuest = user.findUserQuest(quest.getQuestType());
                String status = progress((int) ((userQuest.getAmountProgress() * 1.0) / quest.getProgress()), (int) (10 - ((userQuest.getAmountProgress() * 1.0) / quest.getProgress())));
                double progressLine = ((userQuest.getAmountProgress() * 1.0) / quest.getProgressLine()) * 100;

                ItemHelper questItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                        .setOwnerUrl(quest.getMaterialSkullUrl())
                        .setName(this.questData.getInventoryName().replace("{LEVEL}", String.valueOf(quest.getId())))
                        .setLore(Arrays.stream(this.questData.getInventoryLore())
                                .map(s -> {
                                    s = s.replace("{ACTIVE}", (userQuest.isActive() ? "&8(&cNIE&7, &c0sek&8)" : "&8(&aTAK&7, &a" + DataHelper.getTimeToString(userQuest.getActiveTime()) + "&8)"));
                                    s = s.replace("{DESCRIPTION}", quest.getDescription());
                                    s = s.replace("{TIME-LEFT}", DataHelper.getTimeToString(System.currentTimeMillis() + TimeType.MINUTE.getTime(quest.getQuestMinuteLeft())));
                                    s = s.replace("{REWARD}", quest.getItemParserReward());
                                    s = s.replace("{DONE}", (userQuest.getAmountProgress() < quest.getAmountRequired() ? "&cNIE" : "&aTAK"));
                                    s = s.replace("{PICKED}", (!userQuest.isPicked() ? "&cNIE" : "&aTAK"));
                                    s = s.replace("{PROGRESS}", (userQuest.getAmountProgress() < quest.getAmountRequired() ? String.valueOf(Math.round(progressLine)) : "&aOdbierz swoje zadanie!"));
                                    s = s.replace("{ONE-LINER}", (userQuest.getAmountProgress() < quest.getAmountRequired() ? status : " "));
                                    s = s.replace("%", (userQuest.getAmountProgress() < quest.getAmountRequired() ? "%" : " "));
                                    s = s.replace("{QUEST-PROGRESS}", String.valueOf(userQuest.getAmountProgress()));
                                    return s;
                                })
                                .collect(Collectors.toList()));

                inventory.setItem(quest.getInventorySlot(), questItem.toItemStack());
            }
            this.onClick(player, event -> {
                event.setCancelled(true);


                Quest quest = this.questData.findQuestBySlot(event.getSlot());
                UserQuest userQuestFind = user.findUserQuestActive();
                if (quest != null) {
                    if (userQuestFind != null) {
                        player.closeInventory();
                        player.sendTitle(MessageHelper.colored("&5&lZadania"),
                                MessageHelper.colored("&cJuż wykonujesz jakieś zadanie!!!"));
                        return;
                    }
                    UserQuest userQuest = user.findUserQuest(quest.getQuestType(), quest.getId());
                    if (userQuest != null) {
                        switch (event.getClick()) {
                            case LEFT: {
                                if (userQuest.isDone()) {
                                    player.closeInventory();
                                    player.sendTitle(MessageHelper.colored("&5&lZadania"),
                                            MessageHelper.colored("&aJuż wykonałeś to zadanie"));
                                    return;
                                }
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
                                userQuest.setActiveTime(System.currentTimeMillis() + TimeType.MINUTE.getTime(quest.getQuestMinuteLeft()));
                                player.sendTitle(MessageHelper.colored("&5&lZadania"),
                                        MessageHelper.colored("&aRozpoczęto zadanie&8(&7Spójrz na scoreboard&8)"));
                                break;
                            }
                            case RIGHT: {
                                if (!userQuest.isDone()) {
                                    player.closeInventory();
                                    player.sendTitle(MessageHelper.colored("&5&lZadania"),
                                            MessageHelper.colored("&cWykonaj te zadanie do końca!!"));
                                    return;
                                }
                                if (userQuest.isPicked()) {
                                    player.closeInventory();
                                    player.sendTitle(MessageHelper.colored("&5&lZadania"),
                                            MessageHelper.colored("&cOdebrałeś już to zadanie!!"));
                                    return;
                                }
                                if (!userQuest.isPicked()) {
                                    player.closeInventory();
                                    userQuest.setPicked(true);
                                    player.sendTitle(MessageHelper.colored("&5&lZadania"),
                                            MessageHelper.colored("&aOdebrano pomyślnie zadanie"));
                                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 10f, 10f);
                                    return;
                                }
                                break;
                            }
                        }
                    }
                }

            });
        });
    }
    public String progress(int green, int red) {
        String s = "";
        s += "&a";
        s += stringMultiply("■", green);
        s += "&c";
        s += stringMultiply("■", red);
        return s;
    }
    public String stringMultiply(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}
