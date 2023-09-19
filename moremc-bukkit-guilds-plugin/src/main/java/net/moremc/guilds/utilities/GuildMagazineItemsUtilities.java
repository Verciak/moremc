package net.moremc.guilds.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.bukkit.api.serializer.ItemSerializer;

public class GuildMagazineItemsUtilities {


    public static int countMaterial(GuildGenerator generator){
        ItemStack[] itemStackInMagazine = ItemSerializer.encodeItem(generator.getMagazine().getSerializedInventory());
        int count = 0;
        for (ItemStack itemStack : itemStackInMagazine) {
            if(itemStack == null || !itemStack.getType().equals(Material.valueOf(generator.getGeneratorType().name())))continue;
            int amount = itemStack.getAmount();
            count += amount;
        }
        return count;
    }

}
