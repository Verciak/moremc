package net.moremc.bukkit.tools.inventories.achievement;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.moremc.api.API;
import net.moremc.api.data.achievement.Achievement;
import net.moremc.api.data.achievement.AchievementData;
import net.moremc.api.entity.user.type.UserAchievementType;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.communicator.plugin.CommunicatorPlugin;

import java.util.Arrays;

public class AchievementInventory extends InventoryHelperImpl {


    private final AchievementData achievementJson = CommunicatorPlugin.getInstance().getAchievementData();
    private final UserService userService = API.getInstance().getUserService();

    public AchievementInventory() {
        super("&dOsiągniecia", 27);
    }
    public void init(Player player) {
        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {
            if(this.achievementJson == null){
                player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodczas konfiguracji osiągnięć zgłoś to admininstracji!!!"));
                return;
            }
            for (Achievement achievement : this.achievementJson.getAchievements()) {
                if(achievement == null){
                    player.sendMessage(MessageHelper.translateText("&4Błąd: &cPodczas konfiguracji osiągnięć zgłoś to admininstracji!!"));
                    continue;
                }
                user.addAchievement(achievement);
            }
        });
    }
    @Override
    public void initializeInventory(Player player, Inventory inventory) {
        this.init(player);
        Integer[] glassYellowSlots = new Integer[]{1, 3, 5, 7, 9, 17, 20, 24};
        Integer[] glassBlackSlots = new Integer[]{2, 4, 6, 19, 21, 23, 25};
        Integer[] glassWhiteSlots = new Integer[]{0, 8, 18, 26, 22};

        Arrays.stream(glassYellowSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 2).setName(" ").toItemStack()));
        Arrays.stream(glassBlackSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 15).setName(" ").toItemStack()));
        Arrays.stream(glassWhiteSlots).forEach(slot -> inventory.setItem(slot, new ItemHelper(Material.STAINED_GLASS_PANE, 1, (short) 0).setName(" ").toItemStack()));


        ItemHelper achievementItem = new ItemHelper(Material.STONE)
                .setName("&7Wykopany: &fKamień");

        inventory.setItem(10, achievementItem.toItemStack());

        achievementItem = new ItemHelper(Material.OBSIDIAN)
                .setName("&7Wykopany: &5Obsydian");

        inventory.setItem(11, achievementItem.toItemStack());

        achievementItem = new ItemHelper(Material.SKULL_ITEM, 1, (short) 3)
                .setOwner(player.getName())
                .setName("&7Twoje: &cŚmierci");

        inventory.setItem(12, achievementItem.toItemStack());

        achievementItem = new ItemHelper(Material.DIAMOND_SWORD)
                .setName("&7Twoje: &aZabójstwa");

        inventory.setItem(13, achievementItem.toItemStack());

        achievementItem = new ItemHelper(Material.FISHING_ROD)
                .setName("&7Twoje: &dZłowione ryby");

        inventory.setItem(14, achievementItem.toItemStack());

        achievementItem = new ItemHelper(Material.ENDER_PEARL)
                .setName("&7Twoje: &5Rzucone perły");

        inventory.setItem(15, achievementItem.toItemStack());

        achievementItem = new ItemHelper(Material.GOLDEN_APPLE, 1, (short) 1)
                .setName("&7Twoje: &dZjedzone koxy");

        inventory.setItem(16, achievementItem.toItemStack());


        this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

            this.onClick(player, event -> {
                event.setCancelled(true);

                switch (event.getSlot()) {
                    case 10: {
                        new AchievementTypeInventory(UserAchievementType.STONE_BREAK, user.getBreakStone()).show(player);
                        break;
                    }
                    case 11: {
                        new AchievementTypeInventory(UserAchievementType.OBSIDIAN_BREAK, user.getBreakObsidian()).show(player);
                        break;
                    }
                    case 12: {
                        new AchievementTypeInventory(UserAchievementType.DEATHS, user.getUserStatics().getDeaths()).show(player);
                        break;
                    }
                    case 13: {
                        new AchievementTypeInventory(UserAchievementType.KILLS, user.getUserStatics().getKills()).show(player);
                        break;
                    }
                    case 14: {
                        new AchievementTypeInventory(UserAchievementType.FISHING, user.getFishingAmount()).show(player);
                        break;
                    }
                    case 15: {
                        new AchievementTypeInventory(UserAchievementType.ENDER_PEARL, user.getEnderPearlAmount()).show(player);
                        break;
                    }
                    case 16: {
                        new AchievementTypeInventory(UserAchievementType.GOLDEN_APPLE, user.getConsumeApple()).show(player);
                        break;
                    }
                }

            });
        });
    }
}
