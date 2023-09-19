package net.moremc.bukkit.api.helper;

import java.util.Random;

public class RandomHelper {

    private static final Random random = new Random();

    public static int getInt(int min, int max){
        if(min == max) return min;
        else if(min > max) return getInt(max, min);
        else return random.nextInt(max - min + 1) + min;
    }

    public double getDouble(double min, double max){
        if(min == max) return min;
        else if(min > max) return getDouble(max, min);
        else return random.nextDouble() * (max - min) + min;
    }

    public float getFloat(float min, float max){
        if(min == max) return min;
        else if(min > max) return getFloat(max, min);
        else return random.nextFloat() * (max - min) + min;
    }

    public boolean getChance(double chance){
        return chance >= 100.0 || chance >= getDouble(0, 100);
    }
}