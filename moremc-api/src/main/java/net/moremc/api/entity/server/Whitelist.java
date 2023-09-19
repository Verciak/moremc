package net.moremc.api.entity.server;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Whitelist implements Serializable {

    private String reason;
    private final Set<String> members;
    private boolean status;

    public Whitelist() {
        this.reason = "Serwer tymczasowo jest niedostępny dla graczy.";
        this.members = new HashSet<>();
        this.status = false;
    }

    public Set<String> getMembers() {
        return members;
    }

    public String getReason() {
        return reason;
    }

    public boolean isStatus() {
        return status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
