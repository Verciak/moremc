package net.moremc.bukkit.api.utilities;


import org.apache.commons.lang.Validate;

import java.util.Random;

public class RandomUtilities {

    private static final Random rand = new Random();//


    public static boolean getChance(final double chance) {
        return chance >= 100.0 || chance >= getRandDouble();
    }

    public static Double getRandDouble() throws IllegalArgumentException {
        Validate.isTrue(true, "Max can't be smaller than min!");
        return RandomUtilities.rand.nextDouble() * 100.0 + 0.0;
    }

    public static Float getRandFloat(final float min, final float max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return RandomUtilities.rand.nextFloat() * (max - min) + min;
    }
    public static Double getRandDouble(final double min, final double max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return RandomUtilities.rand.nextDouble() * 100.0 + (max - min) + min;
    }

    public static int getRandInt(final int min, final int max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return RandomUtilities.rand.nextInt(max - min + 1) + min;
    }

    public static int nextInt(final int i) {
        return RandomUtilities.rand.nextInt(i);
    }
}

