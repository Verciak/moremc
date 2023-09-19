package net.moremc.bukkit.api.cache;

import net.moremc.bukkit.api.cache.entity.BukkitUser;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BukkitCache {

    private final Set<BukkitUser> bukkitUsers = new HashSet<>();

    public Optional<BukkitUser> findBukkitUserByNickName(String nickName){
      return  this.bukkitUsers
                .stream()
                .filter(bukkitUser -> bukkitUser.getName().equalsIgnoreCase(nickName))
                .findFirst();
    }
    public void delete(BukkitUser bukkitUser){
        this.bukkitUsers.remove(bukkitUser);
    }
    public BukkitUser create(String nickName){
        BukkitUser bukkitUser = new BukkitUser(nickName);
        this.bukkitUsers.add(bukkitUser);
        return bukkitUser;
    }

    public Set<BukkitUser> getBukkitUsers() {
        return bukkitUsers;
    }
}
