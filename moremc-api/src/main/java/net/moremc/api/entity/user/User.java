package net.moremc.api.entity.user;

import net.moremc.api.data.achievement.Achievement;
import net.moremc.api.data.drop.stone.DropStoneDataArray;
import net.moremc.api.data.quest.Quest;
import net.moremc.api.entity.user.type.UserAchievementType;
import net.moremc.api.entity.user.type.UserGroupType;
import net.moremc.api.entity.user.type.UserQuestType;
import net.moremc.api.entity.user.type.UserSettingMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.helper.type.TimeType;
import net.moremc.api.mysql.Identifiable;
import net.moremc.api.serializer.LocationSerializer;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class User implements Serializable, Identifiable<String> {

    private final String nickName;
    private final UUID uuid;


    private boolean vanish = false;
    private boolean god = false;

    private boolean incognito;
    private boolean discord;
    private String discordAuthorId;
    private String discordVerifyCode;

    private String fakeNickname;
    private final UserSector userSector;

    private LocationSerializer locationBeforeCreate = null;
    private boolean guildCreated = false;
    private GuildArea guildAreaSelect = null;
    private boolean guildCreatedLeave = false;
    private boolean viewTerrainGuild = false;
    private long viewTerrainGuildTime = 0L;
    private long selectAreaGuildTime = 0L;

    private final Set<Integer> disableDrops = new HashSet<>();
    private final Set<Integer> disableDropMessages = new HashSet<>();

    private final Map<String, Long> cooldownMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> depositMap = new ConcurrentHashMap<>();

    private final UserStatics userStatics;
    private final UserAntiLogout userAntiLogout = new UserAntiLogout("", 0L);

    private final Set<String> playersTeleportRequest = new HashSet<>();
    private final List<UserSettingMessage> userSettingList;
    private final List<UserAchievement> achievementList = new ArrayList<>();
    private final List<UserHome> userHomeList;
    private final List<UserQuest> userQuestList = new ArrayList<>();

    private int breakStone = 0;
    private int breakObsidian = 0;
    private int consumeApple = 0;
    private int enderPearlAmount = 0;
    private int fishingAmount = 0;

    private boolean periscope = false;

    private UserGroupType groupType = UserGroupType.PLAYER;
    private String latestPrivateMessageNickName = null;

    private String itemShopItems;


    public User(String nickName, UUID uuid){
        this.uuid = uuid;
        this.nickName = nickName;
        this.fakeNickname = nickName;

        this.userSector = new UserSector();
        this.userStatics = new UserStatics();

        this.userHomeList = new ArrayList<>();

        this.userHomeList.add(new UserHome(1, UserGroupType.PLAYER, 19));
        this.userHomeList.add(new UserHome(2, UserGroupType.VIP, 21));
        this.userHomeList.add(new UserHome(3, UserGroupType.YOUTUBER, 23));
        this.userHomeList.add(new UserHome(4, UserGroupType.SVIP, 25));

        this.userSettingList = Arrays.asList(
                new UserSettingMessage(UserSettingMessageType.QUEST, "wiadmości o zakończonych zadaniach", 10),
                new UserSettingMessage(UserSettingMessageType.DEATH, "wiadmośći o śmierci/zabójstwach", 11),
                new UserSettingMessage(UserSettingMessageType.CHAT, "czat globalny", 12),
                new UserSettingMessage(UserSettingMessageType.CASE, "wiadmości o magicznych skrzynkach", 13),
                new UserSettingMessage(UserSettingMessageType.ITEMSHOP, "wiadmości o zakupionych usługach", 14),
                new UserSettingMessage(UserSettingMessageType.PRIVATE, "prywatne wiadomości /msg", 15),
                new UserSettingMessage(UserSettingMessageType.TELEPORT, "wiadomości o teleportacji /tpaccept", 16)
        );
        this.addCooldown("protection", System.currentTimeMillis() + TimeType.MINUTE.getTime(2));
        this.itemShopItems = "null";

        API.getInstance().getUserService().synchronizeUser(SynchronizeType.CREATE, this);
    }
    public UserQuest findUserQuest(UserQuestType questType, int id){
        return this.userQuestList
                .stream()
                .filter(userQuest -> userQuest.getId() == id)
                .filter(userQuest -> userQuest.getUserQuestType().equals(questType))
                .findFirst()
                .orElse(null);
    }
    public UserQuest findUserQuest(UserQuestType questType){
        return this.userQuestList
                .stream()
                .filter(userQuest -> userQuest.getUserQuestType().equals(questType))
                .findFirst()
                .orElse(null);
    }


    public void addAchievement(Achievement achievement){
        if(this.findUserAchievement(achievement.getAchievementType(), achievement.getId()) == null) {
            this.achievementList.add(new UserAchievement(achievement.getId(), achievement.getAchievementType(), achievement.getAmountRequired()));
            API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
        }
    }
    public UserAchievement findUserAchievement(UserAchievementType achievementType, int id){
        return this.achievementList
                .stream()
                .filter(userAchievement -> userAchievement.getId() == id)
                .filter(userAchievement -> userAchievement.getAchievementType().equals(achievementType))
                .findFirst()
                .orElse(null);
    }
    public Optional<UserSettingMessage> findUserSettingBySlot(int inventorySlot){
        return this.userSettingList
                .stream()
                .filter(userSetting -> userSetting.getSlot() == inventorySlot)
                .findFirst();
    }
    public UserSettingMessage findUserSettingByType(UserSettingMessageType settingMessageType){
        return this.userSettingList
                .stream()
                .filter(userSetting -> userSetting.getSettingMessageType().equals(settingMessageType))
                .findFirst()
                .orElse(null);
    }
    public Optional<UserHome> findHomeByInventorySlot(int slot) {
        return this.userHomeList
                .stream()
                .filter(userHome -> userHome.getInventorySlot() == slot)
                .findFirst();
    }

    public int getDepositCount(String depositName){
        if (!this.depositMap.containsKey(depositName)) {
            this.depositMap.put(depositName, 0);
        }
        return this.depositMap.get(depositName);
    }

    public void addDepositItem(String depositName, int value){
        if (!this.depositMap.containsKey(depositName)) {
            this.depositMap.put(depositName, 0);
        }
        this.depositMap.put(depositName, this.depositMap.get(depositName) + value);
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }
    public void removeDepositItem(String depositName, int value){
        if (!this.depositMap.containsKey(depositName)) {
            this.depositMap.put(depositName, 0);
        }
        this.depositMap.put(depositName, this.depositMap.get(depositName) - value);
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }
    public Map<String, Integer> getDepositMap() {
        return depositMap;
    }

    public List<UserAchievement> getAchievementList() {
        return achievementList;
    }

    public UserStatics getUserStatics() {
        return userStatics;
    }

    public void enableDrop(int id){
        this.disableDrops.remove(id);
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }

    public void disableDrop(int id){
        this.disableDrops.add(id);
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }
    public boolean hasDisable(int id){
        return this.disableDrops.contains(id);
    }
    public void enableDropMessage(int id){
        this.disableDropMessages.remove(id);
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }

    public void disableDropMessage(int id){
        this.disableDropMessages.add(id);
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }
    public boolean hasDisableMessage(int id){
        return this.disableDropMessages.contains(id);
    }
    public void enableALLDrop() {
        this.disableDrops.clear();
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }
    public void disableALLDrop(DropStoneDataArray[] drops){
        Arrays.stream(drops).forEach(drop -> {this.disableDrops.add(drop.getId());});
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }
    public boolean hasProtection(){
        return !this.hasCooldown("protection");
    }

    public String getFakeNickname() {
        return fakeNickname;
    }

    public void setFakeNickname(String fakeNickname) {
        this.fakeNickname = fakeNickname;
    }

    public Set<String> getPlayersTeleportRequest() {
        return playersTeleportRequest;
    }

    public List<UserHome> getUserHomeList() {
        return userHomeList;
    }

    public Set<Integer> getDisableDropMessages() {
        return disableDropMessages;
    }

    public List<UserSettingMessage> getUserSettingList() {
        return userSettingList;
    }

    public Set<Integer> getDisableDrops() {
        return disableDrops;
    }

    public Map<String, Long> getCooldownMap() {
        return cooldownMap;
    }
    public boolean hasCooldown(String key) {
        if(!this.cooldownMap.containsKey(key)){
            this.cooldownMap.put(key, 0L);
            return true;
        }
        return this.cooldownMap.get(key) <= System.currentTimeMillis();
    }
    public void addCooldown(String key, long time) {
        this.cooldownMap.put(key, time);
    }
    public long getCooldownTime(String key){
        if(!this.cooldownMap.containsKey(key)){
            this.cooldownMap.put(key, 0L);
        }
        return this.cooldownMap.get(key);
    }

    public void setGod(boolean god) {
        this.god = god;
    }

    public boolean isGod() {
        return god;
    }

    public GuildArea getGuildAreaSelect() {
        return guildAreaSelect;
    }

    public LocationSerializer getLocationBeforeCreate() {
        return locationBeforeCreate;
    }

    public boolean isGuildCreated() {
        return guildCreated;
    }

    public boolean isGuildCreatedLeave() {
        return guildCreatedLeave;
    }

    public boolean hasViewTerrainGuildTime(){
        return this.viewTerrainGuildTime <= System.currentTimeMillis();
    }

    public void setGuildCreatedLeave(boolean guildCreatedLeave) {
        this.guildCreatedLeave = guildCreatedLeave;
    }

    public void setGuildAreaSelect(GuildArea guildAreaSelect) {
        this.guildAreaSelect = guildAreaSelect;
    }

    public void setLocationBeforeCreate(LocationSerializer locationBeforeCreate) {
        this.locationBeforeCreate = locationBeforeCreate;
    }

    public void setGuildCreated(boolean guildCreated) {
        this.guildCreated = guildCreated;
    }

    public String getNickName() {
        return nickName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UserSector getUserSector() {
        return userSector;
    }

    public boolean isViewTerrainGuild() {
        return viewTerrainGuild;
    }

    public void setViewTerrainGuild(boolean viewTerrainGuild) {
        this.viewTerrainGuild = viewTerrainGuild;
    }

    public long getViewTerrainGuildTime() {
        return viewTerrainGuildTime;
    }

    public void setViewTerrainGuildTime(long viewTerrainGuildTime) {
        this.viewTerrainGuildTime = viewTerrainGuildTime;
    }

    public boolean isIncognito() {
        return incognito;
    }

    public void setIncognito(boolean incognito) {
        this.incognito = incognito;
    }

    public String getLatestPrivateMessageNickName() {
        return latestPrivateMessageNickName;
    }

    public void setLatestPrivateMessageNickName(String latestPrivateMessageNickName) {
        this.latestPrivateMessageNickName = latestPrivateMessageNickName;
    }

    public UserGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(UserGroupType groupType) {
        this.groupType = groupType;
    }

    public int getBreakStone() {
        return breakStone;
    }

    public void setBreakStone(int breakStone) {
        this.breakStone = breakStone;
    }

    public int getBreakObsidian() {
        return breakObsidian;
    }

    public void setBreakObsidian(int breakObsidian) {
        this.breakObsidian = breakObsidian;
    }

    public int getConsumeApple() {
        return consumeApple;
    }

    public void setConsumeApple(int consumeApple) {
        this.consumeApple = consumeApple;
    }

    public int getEnderPearlAmount() {
        return enderPearlAmount;
    }

    public void setEnderPearlAmount(int enderPearlAmount) {
        this.enderPearlAmount = enderPearlAmount;
    }

    public int getFishingAmount() {
        return fishingAmount;
    }

    public void setFishingAmount(int fishingAmount) {
        this.fishingAmount = fishingAmount;
    }

    public UserAntiLogout getUserAntiLogout() {
        return userAntiLogout;
    }
    public boolean isVanish() {
        return vanish;
    }

    public void setVanish(boolean vanish) {
        this.vanish = vanish;
    }

    public void addQuest(Quest quest) {
        if (this.findUserQuest(quest.getQuestType(), quest.getId()) == null) {
            this.userQuestList.add(new UserQuest(quest.getId(), quest.getQuestType(), quest.getAmountRequired()));
            API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
        }
    }
    public UserQuest findUserQuestActive() {
        return this.userQuestList
                .stream()
                .filter(userQuest -> !userQuest.isActive())
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getID() {
        return this.nickName;
    }

    public boolean isPeriscope() {
        return periscope;
    }

    public boolean isDiscord() {
        return discord;
    }

    public void setPeriscope(boolean periscope) {
        this.periscope = periscope;
    }

    public String getItemShopItems() {
        return itemShopItems;
    }

    public String getDiscordAuthorId() {
        return discordAuthorId;
    }

    public void setDiscordAuthorId(String discordAuthorId) {
        this.discordAuthorId = discordAuthorId;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }

    public String getDiscordVerifyCode() {
        return discordVerifyCode;
    }

    public void setDiscord(boolean discord) {
        this.discord = discord;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }

    public void setDiscordVerifyCode(String discordVerifyCode) {
        this.discordVerifyCode = discordVerifyCode;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }

    public void setItemShopItems(String itemShopItems) {
        this.itemShopItems = itemShopItems;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, this);
    }

    public long getSelectAreaGuildTime() {
        return selectAreaGuildTime;
    }

    public void setSelectAreaGuildTime(long selectAreaGuildTime) {
        this.selectAreaGuildTime = selectAreaGuildTime;
    }
}
