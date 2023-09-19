package net.moremc.api.entity.guild.permission;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.entity.guild.Guild;
import net.moremc.api.entity.guild.permission.type.GuildPermissionType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GuildPermission implements Serializable {


    private final int id;
    private String name;
    private GuildPermissionType type;
    private final Set<String> players;
    private final Set<Integer> permissions;

    public GuildPermission(int id, String name, GuildPermissionType type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.players = new HashSet<>();
        this.permissions = new HashSet<>();
    }
    public GuildPermissionType getType() {
        return type;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setType(GuildPermissionType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getPermissions() {
        return permissions;
    }
    public boolean has(int id){
        return this.permissions.contains(id);
    }
    public void setAccess(Guild guild, boolean status, int id){
        if(status){
            this.permissions.remove(id);
            guild.synchronize(SynchronizeType.UPDATE);
            return;
        }
        this.permissions.add(id);
        guild.synchronize(SynchronizeType.UPDATE);
    }
}