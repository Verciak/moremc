package net.moremc.api.entity.guild.player.permission;

import net.moremc.api.entity.guild.Guild;
import net.moremc.api.nats.packet.type.SynchronizeType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GuildPermissionPlayer implements Serializable {


    private final Set<Integer> permissions;

    public GuildPermissionPlayer(){
        this.permissions = new HashSet<>();
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
