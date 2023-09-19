package net.moremc.bukkit.api.helper;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import net.moremc.bukkit.api.utilities.RandomUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RandomTeleportHelper {


    private final List<Player> playerList = new ArrayList<>();

    private World world;
    private Location current;

    private int minX, minZ, maxX, maxZ;
    private double distance;

    private Location foundedLocation;

    private Material material;

    private String message = "Could not find a safety location in the world. Please wait few seconds and use teleport.";

    public RandomTeleportHelper(Player player, World world, int minX, int minZ, int maxX, int maxZ){
        if(player != null)
        this.playerList.add(player);
        this.world = world;
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public RandomTeleportHelper(World world, int minX, int minZ, int maxX, int maxZ){
        this(null, world, minX, minZ, maxX, maxZ);
    }

    public RandomTeleportHelper apply(Consumer<RandomTeleportHelper> consumer){
        consumer.accept(this);
        return this;
    }

    public RandomTeleportHelper findPlayers(int distance, int players){
        playerList.clear();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Location location = onlinePlayer.getLocation();
            if (material != null && location.getBlock().getType() != material) continue;
            if (playerList.size() == players) continue;
            Block block = onlinePlayer.getLocation().getBlock();
            if(location.distance(this.getCurrent()) > distance)continue;
            if (!block.getType().equals(Material.WOOD_PLATE)) continue;
            playerList.add(onlinePlayer);
        }
        return this;
    }

    public Reason findLocation(int limiter){
        return findLocation(0, limiter);
    }

    private Reason findLocation(int currentID, int limiter){
        if(currentID > limiter) return Reason.LIMITER;
        int x = RandomUtilities.getRandInt(minX, maxX);
        int z = RandomUtilities.getRandInt(minZ, maxZ);
        Location location = new Location(world, x, 255, z);
        Chunk chunk = location.getChunk();
        if(!chunk.isLoaded()) chunk.load();
        Biome biome = location.getWorld().getBiome(chunk.getX(), chunk.getZ());
        if(biome.equals(Biome.OCEAN) || biome.equals(Biome.DEEP_OCEAN)) {
            findLocation(++currentID, limiter);
        }

        for (int i = 0; i < location.subtract(0, 1, 0).getBlockY(); i++) {
            Block block = location.getBlock();
            if(block == null || block.isLiquid()) continue;
            return Reason.CORRECT;
        }



        return Reason.CORRECT;
    }

    public void teleport(){
        if(foundedLocation == null) return;
        playerList.forEach(player -> player.teleport(foundedLocation));
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public World getWorld() {
        return world;
    }

    public RandomTeleportHelper setWorld(World world) {
        this.world = world;
        return this;
    }

    public Location getCurrent() {
        return current;
    }

    public RandomTeleportHelper setCurrent(Location current) {
        this.current = current;
        return this;
    }

    public int getMinX() {
        return minX;
    }

    public RandomTeleportHelper setMinX(int minX) {
        this.minX = minX;
        return this;
    }

    public int getMinZ() {
        return minZ;
    }

    public RandomTeleportHelper setMinZ(int minZ) {
        this.minZ = minZ;
        return this;
    }

    public int getMaxX() {
        return maxX;
    }

    public RandomTeleportHelper setMaxX(int maxX) {
        this.maxX = maxX;
        return this;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public RandomTeleportHelper setMaxZ(int maxZ) {
        this.maxZ = maxZ;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public RandomTeleportHelper setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RandomTeleportHelper setMessage(String message) {
        this.message = message;
        return this;
    }

    public Location getFoundedLocation() {
        return foundedLocation;
    }

    public RandomTeleportHelper setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public void setFoundedLocation(Location foundedLocation) {
        this.foundedLocation = foundedLocation;
    }

    public void sendMessagePlayers(String message) {
        this.getPlayerList().forEach(player -> player.sendMessage(MessageHelper.colored(message)));
    }

    public static enum Reason {

        LIMITER,
        CORRECT

    }
}