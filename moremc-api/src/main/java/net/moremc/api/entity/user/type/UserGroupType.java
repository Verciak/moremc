package net.moremc.api.entity.user.type;

import net.moremc.api.entity.user.User;

import java.io.Serializable;
import java.util.Arrays;

public enum UserGroupType implements Serializable {

    DEVELOPER("&6Developer", "&6Developer &7{PLAYER}&8: &e{MESSAGE}", 999),
    ROOT("&4Root", "&4Root &7{PLAYER}&8: &c{MESSAGE}", 999),
    HEADADMIN("&4H@", "&4H@ &7{PLAYER}&8: &c{MESSAGE}", 7),
    ADMIN("&cAdmin", "&cAdmin &7{PLAYER}&8: &4{MESSAGE}", 6),
    MODERATOR("&aModerator", "&aModerator &7{PLAYER}&8: &2{MESSAGE}", 5),
    HELPER("&bHelper", "&bHelper &7{PLAYER}&8: &3{MESSAGE}", 4),
    YOUTUBER("&cY&fT", "&cY&fT &8[{POINTS}] &2{GUILD} &7{PLAYER}&8: &f{MESSAGE}", 3),
    SVIP("&6SVIP", "&6SVIP &8[{POINTS}] &2{GUILD} &7{PLAYER}&8: &f{MESSAGE}", 3),
    VIP("&6VIP", "&6VIP &8[{POINTS}] &2{GUILD} &7{PLAYER}&8: &f{MESSAGE}", 2),
    PLAYER("&fGracz", "&8[{POINTS}] &2{GUILD} &7{PLAYER}&8: &f{MESSAGE}", 1);


    private final String prefix;
    private final String chatFormat;
    private final int level;

    UserGroupType(String prefix, String chatFormat, int level){
        this.prefix = prefix;
        this.chatFormat = chatFormat;
        this.level = level;
    }

    public static boolean groupExists(String groupName) {
        return Arrays.stream(UserGroupType.values()).noneMatch(userGroupType -> userGroupType.name().equalsIgnoreCase(groupName));
    }

    public String getPrefix() {
        return prefix;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public int getLevel() {
        return level;
    }
    public static boolean hasPermission(UserGroupType groupType, User user){
        return (user.getGroupType().getLevel() >= groupType.getLevel());
    }
}
