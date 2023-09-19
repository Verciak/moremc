package net.moremc.bukkit.api.generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.bukkit.api.serializer.ItemSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GeneratorPillar {

    private final int x, z;
    private final int count = 0;

    public GeneratorPillar(int x, int z) {
        this.x = x;
        this.z = z;
    }
    public static GeneratorPillar of(Location location) {
        return new GeneratorPillar(location.getBlockX(), location.getBlockZ());
    }
    public int getZ() {
        return z;
    }
    public int getX() {
        return x;
    }
    public void fill(GuildGenerator generator, World world, Material material, int startY, int stopY) {
        int count = (int) IntStream.range(startY, stopY).count();
        generator.setSuccessBlocks(generator.getSuccessBlocks() + count);

        List<ItemStack> itemStackList = this.magazineToList(generator, ItemSerializer.encodeItem(generator.getMagazine().getSerializedInventory()));
        ItemStack itemStack = itemStackList.get(0);

        if (itemStack != null && itemStack.getType().equals(Material.valueOf(generator.getGeneratorType().name())) && !generator.getGeneratorType().equals(GuildGeneratorType.AIR)){
            itemStack.setAmount((itemStack.getAmount() - count / 2));

            if (itemStack.getAmount() <= 0) {
                itemStackList.remove(itemStack);
            }
        }
        if(itemStack != null && generator.getGeneratorType().equals(GuildGeneratorType.AIR)){
            if(itemStack.getType().equals(Material.DIAMOND_PICKAXE) && itemStack.getItemMeta().getEnchants().containsKey(Enchantment.DIG_SPEED)){
                if(itemStack.getItemMeta().getEnchants().get(Enchantment.DIG_SPEED) == 5){
                    itemStack.setDurability((short) (itemStack.getDurability() + count));
                }
            }
        }
        ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
        generator.getMagazine().setSerializedInventory(ItemSerializer.decodeItems(itemStacks));

        for (int i = startY; i < stopY; i++) {
            Block block = world.getBlockAt(this.x, i, this.z);

            switch (generator.getGeneratorType()) {
                case AIR: {
                    if (block.getType() == material
                            || block.getType() == Material.CHEST
                            || block.getType() == Material.TRAPPED_CHEST
                            || block.getType() == Material.ENDER_CHEST || block.getType().equals(Material.OBSIDIAN)) {

                        generator.setSkippedBlocks(generator.getSkippedBlocks() + 1);
                        continue;
                    }
                    block.setType(material, false);
                    break;
                }
                case SAND:
                case OBSIDIAN: {
                    if (!block.getType().equals(Material.AIR)) {
                        generator.setSkippedBlocks(generator.getSkippedBlocks() + 1);
                        continue;
                    }
                    block.setType(material, false);
                    break;
                }
            }
        }
    }
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
    public List<ItemStack> magazineToList(GuildGenerator generator, ItemStack[] itemStacks){
        List<ItemStack> itemStackList = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if(itemStack == null || !generator.getGeneratorType().equals(GuildGeneratorType.AIR) && !itemStack.getType().equals(Material.valueOf(generator.getGeneratorType().name())))continue;
            itemStackList.add(itemStack);
        }
        return itemStackList;
    }


    public int getCount() {
        return count;
    }
}
