package net.moremc.bukkit.api.helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    private static final Class<?> MinecraftServerClass = ReflectionHelper.getNmsClass("MinecraftServer");
    private static final Method getServer = ReflectionHelper.getMethod(MinecraftServerClass, "getServer");
    private static final Field recentTps = ReflectionHelper.getField(MinecraftServerClass, "recentTps");

    private static final RandomHelper randomHelper = new RandomHelper();

    public RandomHelper getRandomHelper() {
        return randomHelper;
    }

    public double[] getTps(){
        return (double[]) ReflectionHelper.invoke(recentTps, ReflectionHelper.invoke(getServer, null));
    }

    public String translateAlternateColorCodes(String text){
        return ChatColor.translateAlternateColorCodes('&', text.replace(">>", "»"));
    }

    public List<String> translateAlternateColorCodes(List<String> textList){
        return textList.stream().map(this::translateAlternateColorCodes).collect(Collectors.toList());
    }

    public List<String> translateAlternateColorCodes(String... texts){
        return Arrays.stream(texts).map(this::translateAlternateColorCodes).collect(Collectors.toList());
    }
    public void sendTranslatedMessageActionBar(Player player, String message){
        PlayerHelper playerHelper = new PlayerHelper(player);
        playerHelper.sendActionbar(message);
    }

    public void sendTranslatedMessage(Player player, String message){
        player.sendMessage(translateAlternateColorCodes(message));
    }

    public void sendTranslatedMessage(CommandSender commandSender, String message){
        commandSender.sendMessage(translateAlternateColorCodes(message));
    }

    public void sendComponentMessage(Player player, TextComponent... textComponents){
        Arrays.asList(textComponents).forEach(player.spigot()::sendMessage);
    }

    public void sendTranslatedMessage(Player player, List<String> message){
        player.sendMessage(translateAlternateColorCodes(String.join("\n", message)));
    }

    public void sendTranslatedMessage(CommandSender commandSender, List<String> message){
        commandSender.sendMessage(translateAlternateColorCodes(String.join("\n", message)));
    }
    public boolean isLeapYear(long year) {
        return isEvenNumber(year / 4);
    }

    public Integer getInt(String value, int ifNotNumber) {
        if (value.toLowerCase().contains("infinity") || value.toLowerCase().contains("nan")) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return ifNotNumber;
        }
    }

    public String cutLength(String value, int length){
        if(value.length() <= length) return value;
        return value.substring(0, length);
    }

    public Integer getInt(String value) {
        return getInt(value, 0);
    }

    public Double getDouble(String value, double ifNotNumber) {
        if (value.toLowerCase().contains("infinity") || value.toLowerCase().contains("nan")) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return ifNotNumber;
        }
    }

    public String getFirstLetter(String text){
        if(text.isEmpty()) return "";
        return text.substring(0, 1);
    }

    public Double getDouble(String value) {
        return getDouble(value, 0);
    }

    public Float getFloat(String value, float ifNotNumber) {
        if (value.toLowerCase().contains("infinity") || value.toLowerCase().contains("nan")) {
            return 0f;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return ifNotNumber;
        }
    }

    public Float getFloat(String value) {
        return getFloat(value, 0);
    }

    public Long getLong(String value, long ifNotNumber) {
        if (value.toLowerCase().contains("infinity") || value.toLowerCase().contains("nan")) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return ifNotNumber;
        }
    }

    public Long getLong(String value) {
        return getLong(value, 0);
    }

    public Short getShort(String value, short ifNotNumber) {
        if (value.toLowerCase().contains("infinity") || value.toLowerCase().contains("nan")) {
            return 0;
        }
        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            return ifNotNumber;
        }
    }

    public Short getShort(String value) {
        return getShort(value, (short) 0);
    }

    public Byte getByte(String value, byte ifNotNumber) {
        if (value.toLowerCase().contains("infinity") || value.toLowerCase().contains("nan")) {
            return 0;
        }
        try {
            return Byte.parseByte(value
            );
        } catch (NumberFormatException e) {
            return ifNotNumber;
        }
    }

    public Byte getByte(String value){
        return getByte(value, (byte) 0);
    }

    public String getMoneyPrefix(double value){
        String df = new DecimalFormat("##.##").format(value);
        return value > 0 && value < 1 ? df + "gr" : df + "zl";
    }

    public boolean isEvenNumber(long number){
        return number % 2 == 0;
    }

    public String lengthReplacer(int length, String split, String text){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(split).append(text);
        }
        return builder.toString().replaceFirst(split, "");
    }

    public List<String> startWith(List<String> values, String argument){
        if(values.isEmpty() || argument.isEmpty()) return new ArrayList<>();

        List<String> subtexts = new ArrayList<>();

        for (String value : values) {
            if(value.regionMatches(false, 0, argument, 0, argument.length()))
                subtexts.add(value);
        }
        return subtexts;
    }

    public long getUpTime(){
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    public String firstToUpperCase(String text){
        StringBuilder nText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            nText.append(i == 0 ? String.valueOf(text.charAt(i)).toUpperCase() : text.charAt(i));
        }
        return nText.toString();
    }

    public boolean locationEquals(Location location1, Location location2){
        return location1.getWorld().equals(location2.getWorld()) &&
                location1.getBlockX() == location2.getBlockX() &&
                location1.getBlockY() == location2.getBlockY() &&
                location1.getBlockZ() == location2.getBlockZ();
    }

    public double getDistance(Location location, double minX, double minZ, double maxX, double maxZ){
        double x = location.getX();
        double z = location.getZ();

        double distWest = Math.abs(minX - x);
        double distEast = Math.abs(maxX - x);

        double distNorth = Math.abs(minZ - z);
        double distSouth = Math.abs(maxZ - z);

        double distX = Math.min(distWest, distEast);
        double distZ = Math.min(distNorth, distSouth);

        return Math.min(distX, distZ);
    }

    public String appendDigit(int value) {
        return value >= 10 ? String.valueOf(value) : "0" + value;
    }

}