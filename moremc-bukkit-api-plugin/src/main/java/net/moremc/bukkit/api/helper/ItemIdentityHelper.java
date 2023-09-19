package net.moremc.bukkit.api.helper;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class ItemIdentityHelper {



  public static String getItemIdentityTagName(ItemStack item, String key){
    net.minecraft.server.v1_8_R3.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
    NBTTagCompound compound = itemClone.getTag();
    if (compound == null) {
      compound = new NBTTagCompound();
    }
    return compound.getString(key);
  }

  public static ItemStack wrapIdentity(ItemStack item, String key, String identity) {
    net.minecraft.server.v1_8_R3.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
    NBTTagCompound compound = itemClone.getTag();
    if (compound == null) {
      compound = new NBTTagCompound();
    }
    compound.setString(key, identity);
    itemClone.setTag(compound);
    return CraftItemStack.asBukkitCopy(itemClone);
  }

  public static boolean compareIdentity(ItemStack item, String identity) {
    net.minecraft.server.v1_8_R3.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
    NBTTagCompound compound = itemClone.getTag();
    return compound != null && compound.hasKey(identity);
  }
}