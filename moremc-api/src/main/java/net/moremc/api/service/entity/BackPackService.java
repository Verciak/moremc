package net.moremc.api.service.entity;

import net.moremc.api.entity.backpack.BackPack;
import net.moremc.api.service.ServiceImpl;

import java.util.Optional;
import java.util.UUID;

public class BackPackService extends ServiceImpl<UUID, BackPack>
{

    public Optional<BackPack> findByUUID(UUID value) {
         return getMap().values()
                .stream()
                .filter(backPack -> backPack.getOwnerUUID().equals(value))
                .findFirst();
    }
}
