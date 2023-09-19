package net.moremc.bukkit.tools.runnable.player;

import net.moremc.bukkit.tools.utilities.ItemUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.API;
import net.moremc.api.service.entity.UserService;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.tools.ToolsPlugin;

public class PlayerDepositRunnable implements Runnable{


    private final UserService userService = API.getInstance().getUserService();

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.userService.findByValueOptional(player.getName()).ifPresent(user -> {

                int amountEnchantmentApple = ItemUtilities.getAmountOf(player, Material.GOLDEN_APPLE, (short) 1);
                int amountApple = ItemUtilities.getAmountOf(player, Material.GOLDEN_APPLE, (short) 0);
                int amountEnderPearl = ItemUtilities.getAmountOf(player, Material.ENDER_PEARL, (short) 0);
                int amountSnowball = ItemUtilities.getAmountOf(player, Material.SNOW_BALL, (short) 0);
                int amountArrow = ItemUtilities.getAmountOf(player, Material.ARROW, (short) 0);
                int amountEgg = ItemUtilities.getAmountOf(player, Material.EGG, (short) 0);
                int amountIceBlock = ItemUtilities.getAmountOf(player, Material.PACKED_ICE, (short) 0);

                String message =
                                "               &5&lSCHOWEK\n" +
                                "&fPomyślnie przeprowadzono synchronizację przedmiotów\n" +
                                "&fTwoje&8(&5{ITEM}&f, &d{COUNT}&fsztuk&8) &ftrafiły do depozytu\n" +
                                "&fTeraz posidasz&8(&5{ITEM}&f, &d{COUNT_USER}&fsztuk&8) &fw depozycie\n" +
                                "&fAby wypłacić swoje przedmioty do limitu wpisz: &d&l&n/schowek\n&d ";

                if(amountEnchantmentApple > 1){
                    int remove = amountEnchantmentApple - 1;
                    ItemUtilities.remove(new ItemStack(Material.GOLDEN_APPLE, remove, (short) 1), player, remove);
                    user.addDepositItem("enchantment_golden_apple", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "enchantowane złote jabłka")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("enchantment_golden_apple")))));
                }
                if(amountApple > 10){
                    int remove = amountApple - 10;
                    ItemUtilities.remove(new ItemStack(Material.GOLDEN_APPLE, remove, (short) 0), player, remove);
                    user.addDepositItem("golden_apple", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "złote jabłka")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("golden_apple")))));
                }
                if(amountEnderPearl > 3){
                    int remove = amountEnderPearl - 3;
                    ItemUtilities.remove(new ItemStack(Material.ENDER_PEARL,  remove, (short) 0), player, remove);
                    user.addDepositItem("ender_pearl", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "perły endermana")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("ender_pearl")))));
                }
                if(amountSnowball > 8){
                    int remove = amountSnowball - 8;
                    ItemUtilities.remove(new ItemStack(Material.SNOW_BALL,  remove, (short) 0), player, remove);
                    user.addDepositItem("snowball", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "śnieżki")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("snowball")))));
                }
                if(amountEgg > 8){
                    int remove = amountEgg - 8;
                    ItemUtilities.remove(new ItemStack(Material.EGG,  remove, (short) 0), player, remove);
                    user.addDepositItem("egg", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "jajka")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("egg")))));
                }
                if(amountIceBlock > 32){
                    int remove = amountIceBlock - 32;
                    ItemUtilities.remove(new ItemStack(Material.PACKED_ICE,  remove, (short) 0), player, remove);
                    user.addDepositItem("ice_block", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "bloki lodu")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("ice_block")))));
                }
                if(amountArrow > 10){
                    int remove = amountArrow - 10;
                    ItemUtilities.remove(new ItemStack(Material.ARROW,  remove, (short) 0), player, remove);
                    user.addDepositItem("arrow", remove);
                    player.sendMessage(MessageHelper.translateText(message
                            .replace("{ITEM}", "strzały")
                            .replace("{COUNT}", String.valueOf(remove))
                            .replace("{COUNT_USER}", String.valueOf(user.getDepositCount("arrow")))));
                }

            });
        });
    }
    public void start(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(ToolsPlugin.getInstance(), this, 1L, 1L);
    }
}
