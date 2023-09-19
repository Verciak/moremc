package net.moremc.api.service.entity;

import net.moremc.api.entity.guild.GuildArea;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.service.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuildService extends ServiceImpl<String, GuildImpl> {

    private final List<GuildArea> guildAreaList = new ArrayList<>();

    public Optional<GuildArea> findGuildAreaByCord(int x, int z) {
        return this.guildAreaList
                .stream()
                .filter(guildArea -> guildArea.getX() == x)
                .filter(guildArea -> guildArea.getZ() == z)
                .findFirst();
    }
    public Optional<GuildImpl> findGuildByNickName(String nickName) {
        return this.getMap()
                .values()
                .stream()
                .filter(guild -> guild.hasPlayer(nickName) != null)
                .findFirst();
    }
    public Optional<GuildImpl> findGuildByInvite(String nickName) {
        return this.getMap().
                values().
                stream().
                filter(guild -> guild.getPlayerInvites().stream().anyMatch(playerInvite -> playerInvite.equalsIgnoreCase(nickName)))
                .findFirst()
                .map(Optional::of)
                .orElse(null);
    }
    public Optional<GuildArea> findGuildAreaById(int id) {
        return this.guildAreaList
                .stream()
                .filter(guildArea -> guildArea.getId() == id)
                .findFirst();
    }
    public Optional<GuildImpl> findGuildByLocation(String name, int blockX, int blockZ) {
        return this.getMap()
                .values()
                .stream()
                .filter(guild -> guild.getLocation() != null)
                .filter(guild -> guild.isOnCuboid(name, blockX, blockZ))
                .findFirst();
    }
    public List<GuildArea> getGuildAreaList() {
        return guildAreaList;
    }
}
