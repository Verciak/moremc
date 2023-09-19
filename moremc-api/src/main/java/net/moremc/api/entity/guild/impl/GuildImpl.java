package net.moremc.api.entity.guild.impl;

import com.google.gson.Gson;
import net.moremc.api.entity.guild.*;
import net.moremc.api.entity.guild.dues.GuildDues;
import net.moremc.api.entity.guild.generator.GuildGenerator;
import net.moremc.api.entity.guild.generator.GuildGeneratorAir;
import net.moremc.api.entity.guild.generator.GuildGeneratorObsidian;
import net.moremc.api.entity.guild.heart.GuildHeart;
import net.moremc.api.entity.guild.player.GuildPlayer;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.guild.GuildSynchronizePacket;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.entity.guild.*;
import net.moremc.api.entity.guild.generator.GuildGeneratorSand;
import net.moremc.api.entity.guild.generator.type.GuildGeneratorType;
import net.moremc.api.entity.guild.permission.GuildPermission;
import net.moremc.api.entity.guild.permission.type.GuildPermissionType;
import net.moremc.api.entity.guild.player.permission.GuildPermissionPlayer;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;
import net.moremc.api.entity.guild.regeneration.GuildRegeneration;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.mysql.Identifiable;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.serializer.LocationSerializer;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GuildImpl implements Serializable, Guild, Identifiable<String> {


    private final String name;
    private final String fullName;
    private String ownerNickname;


    private  LocationSerializer location;
    private LocationSerializer locationHome;

    private final GuildHeart heart;
    private final GuildStatics statics;
    private final GuildSocial social;
    private final GuildRegeneration regeneration;
    private final GuildPermissionPlayer permission;


    private final Map<String, GuildPlayer> playerMap;
    private final Set<String> playerInvites;

    private final Set<GuildAlly> allies;
    private final Set<GuildAlly> allyInvites;

    private final Set<GuildWar> wars;
    private final Set<GuildWar> warInvites;



    private boolean friendlyFire = true;

    private long protectionTime;
    private long expireTime;

    private int emeraldsCount = 0;

    private final GuildTreasure treasure = new GuildTreasure();

    private final List<GuildGenerator> guildGeneratorList;
    private final List<GuildPermission> permissionList;
    private final List<GuildDues> duesList;

    public GuildImpl(String name, String fullName, String ownerNickname, LocationSerializer location) {
        this.name = name;
        this.fullName = fullName;
        this.ownerNickname = ownerNickname;


        this.location = null;
        this.locationHome = null;

        this.heart = new GuildHeart();
        this.statics = new GuildStatics();
        this.social = new GuildSocial();
        this.regeneration = new GuildRegeneration();
        this.permission = new GuildPermissionPlayer();

        this.playerMap = new ConcurrentHashMap<>();
        this.playerInvites = new HashSet<>();

        this.allies = new HashSet<>();
        this.allyInvites = new HashSet<>();

        this.wars = new HashSet<>();
        this.warInvites = new HashSet<>();

        this.permissionList = new ArrayList<>();
        this.permissionList.add(new GuildPermission(1, "zalozyciel", GuildPermissionType.NORMAL));
        this.permissionList.add(new GuildPermission(2, "zastepca", GuildPermissionType.NORMAL));
        this.permissionList.add(new GuildPermission(3, "mistrz", GuildPermissionType.NORMAL));
        this.permissionList.add(new GuildPermission(4, "czlonek", GuildPermissionType.DEFAULT));

        this.findDefaultPermission().ifPresent(guildPermission -> {
            guildPermission.getPermissions().add(29);
            guildPermission.getPermissions().add(30);
            guildPermission.getPermissions().add(31);
            guildPermission.getPermissions().add(32);
            guildPermission.getPermissions().add(33);
            guildPermission.getPermissions().add(34);
        });

        this.duesList = new ArrayList<>();
        this.duesList.add(new GuildDues(1, 20));
        this.duesList.add(new GuildDues(2, 22));
        this.duesList.add(new GuildDues(3, 24));

        this.addPlayer(new GuildPlayer(ownerNickname, GuildPlayerType.OWNER));

        this.findPermissionByName("zalozyciel").ifPresent(guildPermission -> {
            guildPermission.getPlayers().add(ownerNickname);
        });
        this.protectionTime = System.currentTimeMillis() + TimeType.DAY.getTime(2);
        this.expireTime = System.currentTimeMillis() + TimeType.DAY.getTime(14);
        this.guildGeneratorList = new ArrayList<>();
        this.guildGeneratorList.add(new GuildGeneratorAir(this.name));
        this.guildGeneratorList.add(new GuildGeneratorSand(this.name));
        this.guildGeneratorList.add(new GuildGeneratorObsidian(this.name));
        this.synchronize(SynchronizeType.CREATE);
    }
    public String getGuildLeaderList(){
        List<String> stringList = this.playerMap.values().stream().filter(player -> player.getPlayerType().equals(GuildPlayerType.LEADER)).map(GuildPlayer::getNickName).collect(Collectors.toList());
        return (stringList.size() == 0) ? "Brak" : stringList.toString();
    }
    public String getGuildMasterList(){
        List<String> stringList = this.playerMap.values().stream().filter(player -> player.getPlayerType().equals(GuildPlayerType.MASTER)).map(GuildPlayer::getNickName).collect(Collectors.toList());
        return (stringList.size() == 0) ? "Brak" : stringList.toString();
    }
    public String getOnlinePlayers(){
        List<String> stringList = new ArrayList<>();
        for (GuildPlayer player : this.playerMap.values()) {
            API.getInstance().getUserService().findByValueOptional(player.getNickName()).ifPresent(user -> {
                if(!API.getInstance().getSectorService().isOnlinePlayer(player.getNickName())) stringList.add("&c" + user.getFakeNickname());
                stringList.add("&a" + user.getFakeNickname());
            });
        }
        return stringList.toString().replace("[", "").replace("]", "");
    }
    public int getOnlinePlayersCount(){
        int count = 0;
        for (GuildPlayer player : this.playerMap.values()) {
            if(!API.getInstance().getSectorService().isOnlinePlayer(player.getNickName())) continue;
            count++;
        }
        return count;
    }


    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public long getProtectionTime() {
        return protectionTime;
    }

    public void setProtectionTime(long protectionTime) {
        this.protectionTime = protectionTime;
    }
    public boolean hasProtection(){
        return this.protectionTime <= System.currentTimeMillis();
    }

    public void setLocation(LocationSerializer location) {
        this.location = location;
    }

    @Override
    public void setLocationHome(LocationSerializer location) {
        this.locationHome = location;
    }

    @Override
    public void addSize(int size) {
        this.location.setSize(this.location.getSize() + size);
    }

    @Override
    public void addPlayer(GuildPlayer player) {
        this.playerMap.put(player.getNickName(), player);
    }

    @Override
    public void removePlayer(GuildPlayer player) {
        this.playerMap.remove(player.getNickName(), player);
    }

    @Override
    public GuildPlayer hasPlayer(String nickName) {
        return this.playerMap
                .values()
                .stream()
                .filter(player -> player.getNickName().equalsIgnoreCase(nickName))
                .findFirst()
                .orElse(null);
    }
    public Optional<GuildGenerator> findGuildGeneratorByType(GuildGeneratorType generatorType){
        return this.guildGeneratorList
                .stream()
                .filter(guildGenerator -> guildGenerator.getGeneratorType().equals(generatorType))
                .findFirst();
    }
    public Optional<GuildGenerator> findGuildGeneratorIsActive(){
        return this.guildGeneratorList
                .stream()
                .filter(guildGenerator -> !guildGenerator.isActive())
                .findFirst();
    }
    public Optional<GuildDues> findGuildDuesById(int id){
        return this.duesList
                .stream()
                .filter(guildDues ->  guildDues.getId() == id)
                .findFirst();
    }
    public Optional<GuildDues> findGuildDuesByInventorySlot(int inventorySlot){
        return this.duesList
                .stream()
                .filter(guildDues ->  guildDues.getInventorySlot() == inventorySlot)
                .findFirst();
    }

    @Override
    public void addAlly(GuildAlly ally) {
        this.allies.add(ally);
    }

    @Override
    public void removeAlly(GuildAlly ally) {
        this.allies.remove(ally);
    }

    @Override
    public boolean checkAlly(String guildName) {
        return this.allies.stream().map(ally -> ally.getName().equalsIgnoreCase(guildName)).findFirst().orElse(false);
    }

    @Override
    public void sendInviteAlly(GuildAlly ally) {
        this.allyInvites.add(ally);
    }

    @Override
    public void removeInviteAlly(GuildAlly ally) {
        this.allyInvites.remove(ally);
    }

    @Override
    public boolean hasInviteAlly(String guildName) {
        return this.allyInvites.stream().map(ally -> ally.getName().equalsIgnoreCase(guildName)).findFirst().orElse(false);
    }

    @Override
    public void addPoints(int points) {
        this.statics.setPoints(this.statics.getPoints() + points);
    }

    @Override
    public void removePoints(int points) {
        this.statics.setPoints(this.statics.getPoints() - points);
    }

    @Override
    public void addKills(int kills) {
        this.statics.setKills(this.statics.getKills() + kills);
    }

    @Override
    public void addDeaths(int deaths) {
        this.statics.setDeaths(this.statics.getDeaths() + deaths);
    }

    @Override
    public void addWar(GuildWar war) {
        this.wars.add(war);
    }

    @Override
    public void removeWar(GuildWar war) {
        this.wars.remove(war);
    }

    @Override
    public boolean hasGuildWar(String guildName) {
        return this.wars.stream().map(war -> war.getName().equalsIgnoreCase(guildName)).findFirst().orElse(false);
    }

    @Override
    public void sendInviteWar(GuildWar war) {
        this.warInvites.add(war);
    }

    @Override
    public void removeInviteWar(GuildWar war) {
        this.warInvites.remove(war);
    }

    @Override
    public boolean hasGuildWarInvite(String guildName) {
        return this.warInvites.stream().map(war -> war.getName().equalsIgnoreCase(guildName)).findFirst().orElse(false);
    }

    @Override
    public void synchronize(SynchronizeType type) {
        new ScheduledThreadPoolExecutor(1).schedule(() -> {
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new GuildSynchronizePacket(this.getName(), new Gson().toJson(this), type));
        }, 5L, TimeUnit.MILLISECONDS);
    }

    public String getName() {
        return name;
    }

    public GuildPermissionPlayer getPermission() {
        return permission;
    }

    public GuildRegeneration getRegeneration() {
        return regeneration;
    }

    public GuildSocial getSocial() {
        return social;
    }

    public GuildStatics getStatics() {
        return statics;
    }

    public Set<GuildAlly> getAllies() {
        return allies;
    }

    public Set<GuildAlly> getAllyInvites() {
        return allyInvites;
    }

    public Set<String> getPlayerInvites() {
        return playerInvites;
    }

    public Map<String, GuildPlayer> getPlayerMap() {
        return playerMap;
    }

    public Set<GuildWar> getWarInvites() {
        return warInvites;
    }

    public Set<GuildWar> getWars() {
        return wars;
    }

    public String getFullName() {
        return fullName;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public LocationSerializer getLocation() {
        return location;
    }

    public GuildHeart getHeart() {
        return heart;
    }

    public boolean isInCentrum(LocationSerializer loc, int top, int down, int wall) {
        LocationSerializer c = this.getLocation();
        return c.getY() - down <= loc.getY() && c.getY() + top >= loc.getY() && loc.getX() <= c.getX() + wall && loc.getX() >= c.getX() - wall && loc.getZ() <= c.getZ() + wall && loc.getZ() >= c.getZ() - wall;
    }

    public boolean isOnCuboid(String world, int x, int z) {
        if (!world.equals("world")) {
            return false;
        }
        int distancex = Math.abs(x - this.getLocation().getX());
        int distancez = Math.abs(z - this.getLocation().getZ());
        return distancex - 1 <= this.getLocation().getSize() && distancez - 1 <= this.getLocation().getSize();
    }

    public LocationSerializer getLocationHome() {
        return locationHome;
    }
    public Optional<GuildPlayer> findGuildPlayerByNickname(String nickName){
        return this.playerMap.values()
                .stream()
                .filter(player -> player.getNickName().equalsIgnoreCase(nickName))
                .findFirst();
    }

    public int getCountLeader(){
        return (int) this.playerMap.values().stream().filter(guildPlayer -> guildPlayer.getPlayerType().equals(GuildPlayerType.LEADER)).count();
    }
    public int getCountMaster(){
        return (int) this.playerMap.values().stream().filter(guildPlayer -> guildPlayer.getPlayerType().equals(GuildPlayerType.MASTER)).count();
    }

    public Optional<String> findGuildPlayerByInvite(String nickName){
        return this.playerInvites
                .stream()
                .filter(player -> player.equalsIgnoreCase(nickName))
                .findFirst();
    }

    public Optional<GuildPermission> findPermissionByName(String groupName){
        return this.permissionList
                .stream()
                .filter(permission -> permission.getName().equalsIgnoreCase(groupName))
                .findFirst();
    }
    public Optional<GuildPermission> findPermissionByMember(String member){
        return this.permissionList
                .stream()
                .filter(permission -> permission.getPlayers().contains(member))
                .findFirst();
    }

    public Optional<GuildPermission> findPermissionById(int id){
        return this.permissionList
                .stream()
                .filter(permission -> permission.getId() == id)
                .findFirst();
    }

    public Optional<GuildPermission> findDefaultPermission(){
        return this.permissionList
                .stream()
                .filter(guildPermission -> guildPermission.getType().equals(GuildPermissionType.DEFAULT))
                .findFirst();
    }

    public List<GuildPermission> getPermissionList() {
        return permissionList;
    }

    public void sendMessage(String message) {
        this.playerMap.values().stream().filter(guildPlayer -> API.getInstance().getSectorService().isOnlinePlayer(guildPlayer.getNickName())).forEach(guildPlayer -> {
            ClientSendMessagePacket packet = new ClientSendMessagePacket(message, SendMessageReceiverType.PLAYER, SendMessageType.CHAT);
            packet.setNickNameTarget(guildPlayer.getNickName());
            API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", packet);
        });
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        this.synchronize(SynchronizeType.UPDATE);
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public List<GuildGenerator> getGuildGeneratorList() {
        return guildGeneratorList;
    }

    @Override
    public String getID() {
        return this.name;
    }

    public List<GuildDues> getDuesList() {
        return duesList;
    }

    public int getEmeraldsCount() {
        return emeraldsCount;
    }

    public void setEmeraldsCount(int emeraldsCount) {
        this.emeraldsCount = emeraldsCount;
    }

    public GuildTreasure getTreasure() {
        return treasure;
    }
}
