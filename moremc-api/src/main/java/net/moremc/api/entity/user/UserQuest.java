package net.moremc.api.entity.user;

import net.moremc.api.entity.user.type.UserQuestType;

import java.io.Serializable;

public class UserQuest implements Serializable {

    private final int id;
    private int amountProgress;
    private final  int amountRequired;
    private boolean picked;
    private boolean done;
    private final UserQuestType userQuestType;
    private long activeTime;

    public UserQuest(int id, UserQuestType userQuestType, int amountRequired){
        this.id = id;
        this.amountProgress = 0;
        this.amountRequired = amountRequired;
        this.picked = false;
        this.done = false;
        this.userQuestType = userQuestType;
        this.activeTime = 0L;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

    public boolean isPicked() {
        return picked;
    }

    public boolean isDone() {
        return done;
    }

    public int getId() {
        return id;
    }

    public int getAmountProgress() {
        return amountProgress;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public UserQuestType getUserQuestType() {
        return userQuestType;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public void setAmountProgress(int amountProgress) {
        this.amountProgress = amountProgress;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
    public boolean isActive(){
        return this.activeTime <= System.currentTimeMillis();
    }

}
