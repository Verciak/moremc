package net.moremc.api.entity.user;

import java.io.Serializable;

public class UserAntiLogout implements Serializable {

    private String attackerNickName;
    private long antiLogoutTime;

    public UserAntiLogout(String attackerNickName, long antiLogoutTime){
        this.attackerNickName = attackerNickName;
        this.antiLogoutTime = antiLogoutTime;
    }

    public void setAttackerNickName(String attackerNickName) {
        this.attackerNickName = attackerNickName;
    }

    public void setAntiLogoutTime(long antiLogoutTime) {
        this.antiLogoutTime = antiLogoutTime;
    }

    public long getAntiLogoutTime() {
        return antiLogoutTime;
    }

    public String getAttackerNickName() {
        return attackerNickName;
    }
    public boolean hasAntiLogout(){
        return this.antiLogoutTime <= System.currentTimeMillis();
    }
}
