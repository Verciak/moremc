package net.moremc.api.entity.user;


import net.moremc.api.entity.user.type.UserAchievementType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;

import java.io.Serializable;


public class UserAchievement implements Serializable {

    private final int id;
    private final UserAchievementType achievementType;
    private boolean picked;
    private final int progressRequired;

    public UserAchievement(int id, UserAchievementType achievementType, int progressRequired){
        this.id = id;
        this.achievementType = achievementType;
        this.picked = false;
        this.progressRequired = progressRequired;
    }

    public int getId() {
        return id;
    }

    public int getProgressRequired() {
        return progressRequired;
    }

    public UserAchievementType getAchievementType() {
        return achievementType;
    }

    public UserAchievement setPicked(boolean picked, User user) {
        this.picked = picked;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, user);
        return this;
    }
    public boolean isPicked() {
        return picked;
    }
}