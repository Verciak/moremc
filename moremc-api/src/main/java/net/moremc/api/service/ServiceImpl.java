package net.moremc.api.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceImpl<T, ID> implements Service<T, ID>{


    private final Map<T, ID> map = new ConcurrentHashMap<>();

    @Override
    public ID findByValue(T value) {
        return this.map.get(value);
    }

    @Override
    public Optional<ID> findByValueOptional(T value) {
        return Optional.ofNullable(this.map.get(value));
    }

    @Override
    public ID findOrCreate(T value, ID object) {
        if(!this.map.containsKey(value)){
            this.map.put(value, object);
        }
        return this.findByValue(value);
    }

    @Override
    public void deleteByValue(T value) {
        this.map.remove(value);
    }

    @Override
    public Map<T, ID> getMap() {
        return this.map;
    }
}
