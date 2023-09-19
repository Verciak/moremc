package net.moremc.api.service.entity;

import net.moremc.api.entity.bazaar.Bazaar;
import net.moremc.api.service.ServiceImpl;

import java.util.Optional;

public class BazaarService extends ServiceImpl<Integer, Bazaar>
{
    public Optional<Bazaar> findByNick(String nick) {
        return getMap().values()
                .stream()
                .filter(bazaar -> bazaar.getNickName().equalsIgnoreCase(nick))
                .findFirst();
    }
}
