package net.moremc.api.entity.server;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.mysql.Identifiable;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Serializable, Identifiable<String> {

    private final int id;
    private final Map<String, Boolean> enableMap;
    private final Whitelist whitelist;

    private boolean chatStatus;

    public Server(int id) {
        this.id = id;
        this.enableMap = new ConcurrentHashMap<>();
        this.whitelist = new Whitelist();

        this.chatStatus = true;

        this.enableMap.put("guilds", false);
        this.enableMap.put("kits", false);
        this.enableMap.put("case", false);
        this.enableMap.put("diamond_items", false);

        API.getInstance().getServerService().synchronize(this, SynchronizeType.CREATE);
    }

    public Map<String, Boolean> getEnableMap() {
        return enableMap;
    }
    public Whitelist getWhitelist() {
        return whitelist;
    }

    public boolean isChatStatus() {
        return chatStatus;
    }
    public void setEnable(String enableName, boolean status) {
        enableMap.put(enableName, status);
    }
    public void setChatStatus(boolean status) {
        this.chatStatus = status;
        API.getInstance().getServerService().synchronize(this, SynchronizeType.UPDATE);
    }
    public int getId() {
        return id;
    }

    @Override
    public String getID() {
        return String.valueOf(this.id);
    }
}
