package net.moremc.api.nats.packet.server.callback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackFactory {

    private final Map<Long, Callback> callbackMap = new ConcurrentHashMap<>();

    public void addCallback(long id, Callback callback){
        this.callbackMap.putIfAbsent(id, callback);
    }
    public Callback findCallbackById(long id){
        return this.callbackMap.remove(id);
    }
}
