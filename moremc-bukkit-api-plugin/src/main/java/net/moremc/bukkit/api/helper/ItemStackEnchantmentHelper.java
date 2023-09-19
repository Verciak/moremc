package net.moremc.bukkit.api.helper;

import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemStackEnchantmentHelper {

    private static final Map<String, Enchantment> ENCHANTMENTS_BY_NAME = new HashMap<>();
    private static final Map<String, Enchantment> ENCHANTMENTS_BY_ALIAS = new HashMap<>();
//
    static {
        ENCHANTMENTS_BY_NAME.put("alldamage", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS_BY_ALIAS.put("alldmg", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS_BY_NAME.put("sharpness", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS_BY_ALIAS.put("sharp", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS_BY_ALIAS.put("dal", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS_BY_NAME.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS_BY_NAME.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS_BY_ALIAS.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS_BY_ALIAS.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS_BY_ALIAS.put("dar", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS_BY_NAME.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENTS_BY_NAME.put("smite", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENTS_BY_ALIAS.put("du", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENTS_BY_NAME.put("digspeed", Enchantment.DIG_SPEED);
        ENCHANTMENTS_BY_NAME.put("efficiency", Enchantment.DIG_SPEED);
        ENCHANTMENTS_BY_ALIAS.put("minespeed", Enchantment.DIG_SPEED);
        ENCHANTMENTS_BY_ALIAS.put("cutspeed", Enchantment.DIG_SPEED);
        ENCHANTMENTS_BY_ALIAS.put("ds", Enchantment.DIG_SPEED);
        ENCHANTMENTS_BY_ALIAS.put("eff", Enchantment.DIG_SPEED);
        ENCHANTMENTS_BY_NAME.put("durability", Enchantment.DURABILITY);
        ENCHANTMENTS_BY_ALIAS.put("dura", Enchantment.DURABILITY);
        ENCHANTMENTS_BY_NAME.put("unbreaking", Enchantment.DURABILITY);
        ENCHANTMENTS_BY_ALIAS.put("d", Enchantment.DURABILITY);
        ENCHANTMENTS_BY_NAME.put("thorns", Enchantment.THORNS);
        ENCHANTMENTS_BY_NAME.put("highcrit", Enchantment.THORNS);
        ENCHANTMENTS_BY_ALIAS.put("thorn", Enchantment.THORNS);
        ENCHANTMENTS_BY_ALIAS.put("highercrit", Enchantment.THORNS);
        ENCHANTMENTS_BY_ALIAS.put("t", Enchantment.THORNS);
        ENCHANTMENTS_BY_NAME.put("fireaspect", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS_BY_NAME.put("fire", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS_BY_ALIAS.put("meleefire", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS_BY_ALIAS.put("meleeflame", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS_BY_ALIAS.put("fa", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS_BY_NAME.put("knockback", Enchantment.KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("kback", Enchantment.KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("kb", Enchantment.KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("k", Enchantment.KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS_BY_NAME.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS_BY_ALIAS.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS_BY_ALIAS.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS_BY_ALIAS.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS_BY_NAME.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS_BY_NAME.put("looting", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS_BY_ALIAS.put("lbm", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS_BY_ALIAS.put("oxygen", Enchantment.OXYGEN);
        ENCHANTMENTS_BY_NAME.put("respiration", Enchantment.OXYGEN);
        ENCHANTMENTS_BY_ALIAS.put("breathing", Enchantment.OXYGEN);
        ENCHANTMENTS_BY_NAME.put("breath", Enchantment.OXYGEN);
        ENCHANTMENTS_BY_ALIAS.put("o", Enchantment.OXYGEN);
        ENCHANTMENTS_BY_NAME.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS_BY_ALIAS.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS_BY_NAME.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS_BY_ALIAS.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS_BY_ALIAS.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("bprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("bprotect", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_NAME.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("pe", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS_BY_ALIAS.put("fallprotection", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS_BY_NAME.put("fallprot", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS_BY_NAME.put("featherfall", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS_BY_ALIAS.put("featherfalling", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS_BY_ALIAS.put("pfa", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS_BY_ALIAS.put("fireprotection", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_ALIAS.put("flameprotection", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_NAME.put("fireprotect", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_ALIAS.put("flameprotect", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_NAME.put("fireprot", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_ALIAS.put("flameprot", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_ALIAS.put("pf", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS_BY_NAME.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENTS_BY_NAME.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENTS_BY_ALIAS.put("pp", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENTS_BY_NAME.put("silktouch", Enchantment.SILK_TOUCH);
        ENCHANTMENTS_BY_ALIAS.put("softtouch", Enchantment.SILK_TOUCH);
        ENCHANTMENTS_BY_ALIAS.put("st", Enchantment.SILK_TOUCH);
        ENCHANTMENTS_BY_NAME.put("waterworker", Enchantment.WATER_WORKER);
        ENCHANTMENTS_BY_NAME.put("aquaaffinity", Enchantment.WATER_WORKER);
        ENCHANTMENTS_BY_ALIAS.put("watermine", Enchantment.WATER_WORKER);
        ENCHANTMENTS_BY_ALIAS.put("ww", Enchantment.WATER_WORKER);
        ENCHANTMENTS_BY_ALIAS.put("firearrow", Enchantment.ARROW_FIRE);
        ENCHANTMENTS_BY_NAME.put("flame", Enchantment.ARROW_FIRE);
        ENCHANTMENTS_BY_NAME.put("flamearrow", Enchantment.ARROW_FIRE);
        ENCHANTMENTS_BY_ALIAS.put("af", Enchantment.ARROW_FIRE);
        ENCHANTMENTS_BY_NAME.put("arrowdamage", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS_BY_NAME.put("power", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS_BY_ALIAS.put("arrowpower", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS_BY_ALIAS.put("ad", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS_BY_NAME.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS_BY_NAME.put("punch", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("ak", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS_BY_ALIAS.put("infinitearrows", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_NAME.put("infarrows", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_NAME.put("infinity", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_ALIAS.put("infinite", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_ALIAS.put("unlimited", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_ALIAS.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_ALIAS.put("ai", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS_BY_NAME.put("luck", Enchantment.LUCK);
        ENCHANTMENTS_BY_ALIAS.put("luckofsea", Enchantment.LUCK);
        ENCHANTMENTS_BY_ALIAS.put("luckofseas", Enchantment.LUCK);
        ENCHANTMENTS_BY_ALIAS.put("rodluck", Enchantment.LUCK);
        ENCHANTMENTS_BY_NAME.put("lure", Enchantment.LURE);
        ENCHANTMENTS_BY_ALIAS.put("rodlure", Enchantment.LURE);
        ENCHANTMENTS_BY_NAME.put("depthstrider", Enchantment.DEPTH_STRIDER);
        ENCHANTMENTS_BY_ALIAS.put("depth", Enchantment.DEPTH_STRIDER);
        ENCHANTMENTS_BY_ALIAS.put("strider", Enchantment.DEPTH_STRIDER);
    }

    private ItemStackEnchantmentHelper() {
    }

    public static Enchantment find(String name) {
        List<Enchantment> enchantments = Arrays.asList(Enchantment.getByName(name.toUpperCase()),
                Enchantment.getByName(name.toLowerCase()), Enchantment.getByName(name),
                ENCHANTMENTS_BY_NAME.get(name.toLowerCase()),
                ENCHANTMENTS_BY_ALIAS.get(name.toLowerCase()));
        for (Enchantment enchantment : enchantments) {
            if (enchantment != null) {
                return enchantment;
            }
        }

        return null;
    }
}
