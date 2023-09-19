
package net.moremc.bukkit.api.cache.entity;

import net.moremc.bukkit.api.helper.*;
import net.moremc.bukkit.api.helper.type.UserActionBarType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BukkitUser {

    private String name;
    private UUID uuid;

    private Player player;

    private PlayerHelper playerHelper;
    private TablistHelper tablistHelper;
    private ScoreboardHelper scoreboardHelper;
    private PacketHelper packetHelper;
    private long timeShooterDelay = 0L;
    private List<ArmorStandHelper> armorStandHelperList;
    transient Map<UserActionBarType, String> actionBarMap = new LinkedHashMap<>();

    private boolean openCase = false;

    public BukkitUser(String name){
        this.name = name;
    }

    public Object getEntityPlayer(){
        return packetHelper.getEntityPlayer();
    }

    public void init(Player player){
        this.player = player;
        this.uuid = player.getUniqueId();

        this.playerHelper = new PlayerHelper(player);
        this.packetHelper = new PacketHelper(player);
        this.tablistHelper = new TablistHelper(player, packetHelper);
        this.scoreboardHelper = new ScoreboardHelper(player, packetHelper);

        this.armorStandHelperList = Arrays.asList(
                new ArmorStandHelper(player, packetHelper),
                new ArmorStandHelper(player, packetHelper),
                new ArmorStandHelper(player, packetHelper),
                new ArmorStandHelper(player, packetHelper),
                new ArmorStandHelper(player, packetHelper)
                );

    }

    public List<ArmorStandHelper> getArmorStandHelperList() {
        return armorStandHelperList;
    }

    public Map<UserActionBarType, String> getActionBarMap() {
        return actionBarMap;
    }

    public ScoreboardHelper getScoreboardHelper() {
        return scoreboardHelper;
    }

    public long getTimeShooterDelay() {
        return timeShooterDelay;
    }

    public void setTimeShooterDelay(long timeShooterDelay) {
        this.timeShooterDelay = timeShooterDelay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public TablistHelper getTablistHelper() {
        return tablistHelper;
    }

    public void setTablistHelper(TablistHelper tablistHelper) {
        this.tablistHelper = tablistHelper;
    }

    public PacketHelper getPacketHelper() {
        return packetHelper;
    }

    public void setPacketHelper(PacketHelper packetHelper) {
        this.packetHelper = packetHelper;
    }

    public PlayerHelper getPlayerHelper() {
        return playerHelper;
    }

    public void setPlayerHelper(PlayerHelper playerHelper) {
        this.playerHelper = playerHelper;
    }

    public void updateActionBar(UserActionBarType type, String text) {
        synchronized (this.actionBarMap) {
            this.actionBarMap.put(type, text);
        }
    }
    public void updateActionBar(UserActionBarType type, String text, int seconds) {
        synchronized (this.actionBarMap) {

            if(!this.actionBarMap.containsKey(type)) {
                new ScheduledThreadPoolExecutor(1).schedule(() -> {
                    this.removeActionBar(type);
                }, seconds, TimeUnit.SECONDS);
            }
            this.actionBarMap.put(type, text);
        }
    }

    public void removeActionBar(UserActionBarType type) {
        synchronized (this.actionBarMap) {
            this.actionBarMap.remove(type);
        }
    }

    public void clearActionBars() {
        synchronized (this.actionBarMap) {
            this.actionBarMap.clear();
        }
    }
    public String collectActiveActionBars() {
        return this.actionBarMap.values().toString().replace("[", "").replace("]", "");
    }

    @Override
    public String toString() {
        return "BukkitUser{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", player=" + player +
                ", playerHelper=" + playerHelper +
                ", tablistHelper=" + tablistHelper +
                ", packetHelper=" + packetHelper +
                '}';
    }

    public boolean isOpenCase() {
        return openCase;
    }

    public void setOpenCase(boolean openCase) {
        this.openCase = openCase;
    }
}