package net.moremc.bukkit.tools.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.data.achievement.Achievement;
import net.moremc.bukkit.api.helper.ItemHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class AchievementRewardUtilities {

    private final ItemHelper boyFarmerItem = new ItemHelper(Material.ENDER_PORTAL_FRAME);
    private final ItemHelper sandFarmerItem = new ItemHelper(Material.SANDSTONE);
    private final ItemHelper caseItem = new ItemHelper(Material.ENDER_CHEST);
    private final ItemHelper goldenAppleItem = new ItemHelper(Material.GOLDEN_APPLE, 1, (short) 1);
    private final ItemHelper enderPearlItem = new ItemHelper(Material.ENDER_PEARL);

    private final ItemStack cobblexItem = ToolsPlugin.getInstance().getCustomItemService().find("cobblex");

    public void pickAchievementPlayer(Player player, Achievement achievement){
        switch (achievement.getRewardType()){
            case CASE:{
                ItemUtilities.addItem(player, this.caseItem.setAmount(achievement.getAmountReward()));
                break;
            }
            case NULL:{
                break;
            }
            case COBBLEX:{
                this.cobblexItem.setAmount(achievement.getAmountReward());
                ItemUtilities.addItem(player, this.cobblexItem);
                break;
            }
            case BOYFARMER:{
                ItemUtilities.addItem(player, this.boyFarmerItem.setAmount(achievement.getAmountReward()));
                break;
            }
            case SANDFARMER:{
                ItemUtilities.addItem(player, this.sandFarmerItem.setAmount(achievement.getAmountReward()));
                break;
            }
            case ENDER_PEARL:{
                ItemUtilities.addItem(player, this.enderPearlItem.setAmount(achievement.getAmountReward()));
                break;
            }
            case GOLDEN_APPLE:{
                ItemUtilities.addItem(player, this.goldenAppleItem.setAmount(achievement.getAmountReward()));
                break;
            }
        }
    }
}