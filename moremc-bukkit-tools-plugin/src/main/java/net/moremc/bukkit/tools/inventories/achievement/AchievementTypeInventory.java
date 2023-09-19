package net.moremc.bukkit.tools.inventories.achievement;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.data.achievement.AchievementData;
import net.moremc.api.entity.user.UserAchievement;
import net.moremc.api.entity.user.type.UserAchievementType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.tools.utilities.AchievementRewardUtilities;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AchievementTypeInventory extends InventoryHelperImpl {


    private final AchievementData achievementJson = CommunicatorPlugin.getInstance().getAchievementData();
    private final UserService userService = API.getInstance().getUserService();

    private final UserAchievementType achievementType;
    private final int fieldUserAmount;

    public AchievementTypeInventory(UserAchievementType achievementType, int fieldUserAmount) {
        super("&dOsiągniecia", 27);
        this.achievementType = achievementType;
        this.fieldUserAmount = fieldUserAmount;
    }

    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        Integer[] glassYellowSlots = new Integer[]{1, 3, 5, 7, 9, 17, 20, 24};
        Integer[] glassBlackSlots = new Integer[]{2, 4, 6, 19, 21, 23, 25};
        Integer[] glassWhiteSlots = new Integer[]{0, 8, 18, 26};

        Arrays.stream(glassYellowSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(glassBlackSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack()));
        Arrays.stream(glassWhiteSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 0).setName(" ").toItemStack()));


        inventory.setItem(22, new ItemHelper(Material.DARK_OAK_FENCE_GATE).setName("&cPowrót").toItemStack());

        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            Arrays.stream(this.achievementJson.achievements).filter(achievement -> achievement.getAchievementType().equals(achievementType))
                    .collect(Collectors.toList()).forEach(achievement -> {


                        String status = progress((int) ((fieldUserAmount * 1.0) / achievement.getProgress()), (int) (10 - ((fieldUserAmount * 1.0) / achievement.getProgress())));
                        double progressLine = ((fieldUserAmount * 1.0) / achievement.getProgressLine()) * 100;

                        UserAchievement userAchievement = user.findUserAchievement(achievementType, achievement.getId());
                        ItemHelper itemAchievement = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                                .setOwnerUrl(achievement.getMaterialSkullUrl())
                                .setName(this.achievementJson.getInventoryName().replace("{LEVEL}", String.valueOf(achievement.getId())))
                                .setLore(Arrays.stream(this.achievementJson.getInventoryLore())
                                        .map(s -> {
                                            s = s.replace("{REWARD}", achievement.getItemParserReward());
                                            s = s.replace("{ACHIEVED}", (fieldUserAmount < achievement.getAmountRequired() ? "&cNIE" : "&aTAK"));
                                            s = s.replace("{PICKED}", (!userAchievement.isPicked() ? "&cNIE" : "&aTAK"));
                                            s = s.replace("{PROGRESS}", (fieldUserAmount < achievement.getAmountRequired() ? String.valueOf(Math.round(progressLine)) : "&aOdbierz swoje osiągniecie!"));
                                            s = s.replace("{ONE-LINER}", (fieldUserAmount < achievement.getAmountRequired() ? status : " "));
                                            s = s.replace("%", (fieldUserAmount < achievement.getAmountRequired() ? "%" : " "));
                                            s = s.replace("{PROGRESS-ACHIEVEMENT}", String.valueOf(fieldUserAmount));
                                            return s;
                                        }).collect(Collectors.toList()));

                        inventory.setItem(achievement.getInventorySlot(), itemAchievement.toItemStack());

                    });
            this.onClick(player, event -> {
                event.setCancelled(true);

                if(event.getSlot() == 22){
                    new AchievementInventory().show(player);
                    return;
                }
                this.achievementJson.findAchievement(achievementType, event.getSlot()).ifPresent(achievement -> {
                    UserAchievement userAchievement = user.findUserAchievement(achievementType, achievement.getId());

                    if(fieldUserAmount < achievement.getAmountRequired()){
                        player.closeInventory();
                        player.sendTitle(MessageHelper.translateText("&5&lOsiągniecia"),
                                MessageHelper.translateText("&cMusisz wykonać te &4osiągniecie &cdo końca."));
                        return;
                    }
                    if(userAchievement.isPicked()){
                        player.closeInventory();
                        player.sendTitle(MessageHelper.translateText("&5&lOsiągniecia"),
                                MessageHelper.translateText("&cJuż odebrałeś te osiągniecie."));
                        return;
                    }
                    player.closeInventory();
                    userAchievement.setPicked(true, user);
                    new AchievementRewardUtilities().pickAchievementPlayer(player, achievement);
                    player.sendTitle(MessageHelper.translateText("&d&lOsiągniecia"),
                            MessageHelper.translateText(  "&aOdebrano pomyślnie osiągniecie."));
                });
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
