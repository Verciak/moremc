package net.moremc.bukkit.tools.helper;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import net.moremc.api.entity.backpack.BackPack;
import net.moremc.api.entity.backpack.type.BackPackColorType;
import net.moremc.bukkit.api.bulider.ItemBulider;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class BackPackHelper
{
    public static ItemStack getBackPackItem(BackPack backPack, BackPackColorType type) {
        String playerName = Bukkit.getOfflinePlayer(backPack.getOwnerUUID()).getName();

        net.minecraft.server.v1_8_R3.ItemStack backpackItem = CraftItemStack.asNMSCopy(
                new ItemBulider(Material.SKULL_ITEM, 1, (short) 3).setName("&fPlecak: &d" + playerName).setOwnerUrl(type.getValue()).setLore(Arrays.asList(
                        " &8* &fWłaściciel: &d" + playerName,
                        " &8* &fPierwszy właściciel: &d" + playerName,
                        "",
                        " &8* &fData stworzenia: &d" + new SimpleDateFormat("yyyy-mm-dd HH:mm").format(new Date(System.currentTimeMillis())),
                        " &8* &fOpis: " + (backPack.getDescription().isEmpty() ? "&cBrak" : "&d" + backPack.getDescription()),
                        " &8* &fW plecaku pomieści się: &d54 &fprzedmiotów",
                        ""
                )).toItemStack());
        NBTTagCompound tag = !Objects.nonNull(backpackItem.getTag()) ? new NBTTagCompound() : backpackItem.getTag();

        tag.setString("backpack-uuid", backPack.getFirstOwnerUUID().toString());
        backpackItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(backpackItem);
    }
}
