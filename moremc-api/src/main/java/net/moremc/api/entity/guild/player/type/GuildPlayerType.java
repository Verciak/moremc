package net.moremc.api.entity.guild.player.type;

import java.io.Serializable;

public enum GuildPlayerType implements Serializable {

    OWNER(1),
    LEADER(2),
    MASTER(3),
    MEMBER(99999);

    private final int limit;

    GuildPlayerType(int limit){
        this.limit = limit;
    }
    public int getLimit() {
        return limit;
    }
    public static boolean hasPermissionMaster(GuildPlayerType playerType){
        return playerType.equals(MASTER) || playerType.equals(LEADER) || playerType.equals(OWNER);
    }
    public static boolean hasPermissionOwner(GuildPlayerType playerType){
        return playerType.equals(OWNER);
    }
    public static boolean hasPermissionLeader(GuildPlayerType playerType){
        return playerType.equals(LEADER) || playerType.equals(OWNER);
    }
}
