package net.moremc.api.entity.ban;


import net.moremc.api.mysql.Identifiable;

import java.io.Serializable;

public class Ban implements Serializable, Identifiable<String> {

    private final String nickName;
    private final String adminNickName;
    private final String addressHostName;
    private final String reason;
    private final long tempDelay;

    public Ban(String nickName, String adminNickName, String addressHostName, String reason, long tempDelay) {
        this.nickName = nickName;
        this.adminNickName = adminNickName;
        this.addressHostName = addressHostName;
        this.reason = reason;
        this.tempDelay = tempDelay;
    }
    public String getNickName() {
        return nickName;
    }

    public String getAdminNickName() {
        return adminNickName;
    }

    public String getAddressHostName() {
        return addressHostName;
    }

    public String getReason() {
        return reason;
    }

    public long getTempDelay() {
        return tempDelay;
    }

    @Override
    public String toString() {
        return "Ban{" +
                "nickName='" + nickName + '\'' +
                ", adminNickName='" + adminNickName + '\'' +
                ", addressHostName='" + addressHostName + '\'' +
                ", reason='" + reason + '\'' +
                ", tempDelay=" + tempDelay +
                '}';
    }
    @Override
    public String getID() {
        return this.nickName;
    }
}
