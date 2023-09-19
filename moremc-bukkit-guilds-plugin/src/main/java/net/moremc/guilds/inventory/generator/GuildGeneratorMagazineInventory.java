package net.moremc.guilds.inventory.generator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.entity.guild.generator.magazine.GuildGeneratorMagazine;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.bukkit.api.helper.MessageHelper;
import net.moremc.bukkit.api.inventory.impl.InventoryHelperImpl;
import net.moremc.bukkit.api.serializer.ItemSerializer;

public class GuildGeneratorMagazineInventory extends InventoryHelperImpl {

    private final GuildImpl guild;
    private final GuildGenerator guildGenerator;

    public GuildGeneratorMagazineInventory(GuildImpl guild, GuildGenerator guildGenerator) {
        super("&dMagazyn", 54);
        this.guild = guild;
        this.guildGenerator = guildGenerator;
    }
    @Override
    public void initializeInventory(Player player, Inventory inventory) {

        GuildGeneratorMagazine generatorMagazine = guildGenerator.getMagazine();
        if(generatorMagazine.getSerializedInventory().equalsIgnoreCase("null")){
            generatorMagazine.setSerializedInventory(this.guild, ItemSerializer.decodeItems(new ItemStack[54]));
        }
        for (ItemStack itemStack : ItemSerializer.encodeItem(generatorMagazine.getSerializedInventory())) {
            if(itemStack == null || itemStack.getType().equals(Material.AIR))continue;
            inventory.addItem(itemStack);
        }

        this.onClose(player, event -> {
            Inventory inventoryClose = event.getInventory();

            for (ItemStack itemStack : inventoryClose.getContents()) {
                if (itemStack != null){
                    if(this.guildGenerator.getGeneratorType().equals(GuildGeneratorType.AIR) && !itemStack.getType().equals(Material.DIAMOND_PICKAXE)){
                        player.getInventory().addItem(itemStack);
                        inventory.removeItem(itemStack);
                    }
                    if(!this.guildGenerator.getGeneratorType().equals(GuildGeneratorType.AIR) && !itemStack.getType().equals(Material.valueOf(this.guildGenerator.getGeneratorType().name()))){
                        player.getInventory().addItem(itemStack);
                        inventory.removeItem(itemStack);
                    }
                }
            }
            generatorMagazine.setSerializedInventory(guild, ItemSerializer.decodeItems(inventoryClose.getContents()));
            player.sendMessage(MessageHelper.translateText("&fPomyślnie &dzapisano &fmagazyn &dgeneratora&8."));
        });

    }
}
