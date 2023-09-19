package net.moremc.bukkit.api.bulider;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.moremc.bukkit.api.helper.ItemIdentityHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBulider
{

    private ItemStack item;

    public ItemBulider(Material material){
        this.item = new ItemStack(material);
    }


    public ItemBulider(Material material, int amount){
        this.item = new ItemStack(material, amount);
    }
    public ItemBulider(Material material, short dura){
        this.item = new ItemStack(material, 1, dura);
    }
    public ItemBulider(Material material, int amount, short dura){
        this.item = new ItemStack(material, amount, dura);
    }
    public ItemBulider(ItemStack itemStack){
        this.item = itemStack;
    }
    public ItemBulider(ItemStack itemStack, int amount){
        itemStack.setAmount(amount);
        this.item = itemStack;
    }
    public ItemBulider setName(String name){
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(color(name));
        this.item.setItemMeta(meta);
        return this;
    }
    public ItemBulider setAmount(int amount){
        this.item.setAmount(amount);
        return this;
    }
    public ItemBulider setDura(short dura){
        this.item.setDurability(dura);
        return this;
    }
    public ItemBulider setOwner(String owner){
        SkullMeta meta = (SkullMeta)this.item.getItemMeta();
        meta.setDisplayName(color(owner));
        meta.setOwner(owner);
        item.setItemMeta(meta);
        return this;
    }
    public ItemBulider addLore(String lore){
        ItemMeta meta = this.item.getItemMeta();
        List<String> list = new ArrayList();
        if(meta.hasLore()){
            list = new ArrayList(meta.getLore());
        }
        list.add(lore);
        meta.setLore(color(list));
        item.setItemMeta(meta);
        return this;
    }
    public ItemBulider setLore(List<String>  lore){
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(color(lore));
        this.item.setItemMeta(meta);
        return this;
    }
    public ItemBulider addEnchant(Enchantment enchant, int level){
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchant, level, true);
        this.item.setItemMeta(meta);
        return this;
    }
    public ItemStack toItemStack(){
        return this.item;
    }

    private static String color(String text){
        return text == null ? "" : ChatColor.translateAlternateColorCodes('&', text)
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("{STATUS}", "■");
    }
    private static List<String> color(List<String> textList){
        return textList.stream().map(ItemBulider::color).collect(Collectors.toList());
    }
    public ItemBulider visibleFlag(){
        ItemMeta meta = toItemStack().getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        toItemStack().setItemMeta(meta);
        return this;
    }
    public ItemBulider setOwnerUrl(String url) {
        ItemStack head = this.item;
        if (url.isEmpty()) return this;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return this;
    }


    public void setItemMeta(SkullMeta headMeta) {
        ItemStack itemStack = (ItemStack) headMeta;
        itemStack.setItemMeta(headMeta);
    }
    public ItemBulider addEnchant(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach((enchant, power) -> {this.addEnchant(enchant, power);});
        return this;
    }

    public ItemBulider setLore(String[] toArray) {
        for (String text : toArray) {
            this.addLore(text);
        }
        return this;
    }

    public ItemBulider setIdentifyItem(String key, String identity){
        this.item = ItemIdentityHelper.wrapIdentity(this.item, key, identity);
        return this;
    }
}
//