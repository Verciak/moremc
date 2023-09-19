package net.moremc.api.service;

import java.util.Map;
import java.util.Optional;

public interface Service<T, ID> {


    Map<T, ID> getMap();

    ID findByValue(T value);

    Optional<ID> findByValueOptional(T value);

    ID findOrCreate(T value, ID object);

    void deleteByValue(T value);


}
