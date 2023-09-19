package net.moremc.api.entity.guild.player;

import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.guild.player.permission.GuildPermissionPlayer;
import net.moremc.api.entity.guild.player.type.GuildPlayerType;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

public class GuildPlayer implements Serializable {

    private final String nickName;
    private final GuildPermissionPlayer permission;
    private GuildPlayerType playerType;


    public GuildPlayer(String nickName, GuildPlayerType playerType){
        this.nickName =nickName;
        this.playerType = playerType;
        this.permission = new GuildPermissionPlayer();
    }

    public void setPlayerType(GuildPlayerType playerType) {
        this.playerType = playerType;
    }

    public String getNickName() {
        return nickName;
    }

    public GuildPlayerType getPlayerType() {
        return playerType;
    }
    public GuildPermissionPlayer getPermission() {
        return permission;
    }
    public boolean hasPermission(GuildImpl guild, int id){
        AtomicBoolean status = new AtomicBoolean(false);
        guild.findPermissionByMember(this.nickName).ifPresent(guildPermission -> {
            guild.findPermissionByName(guildPermission.getName()).ifPresent(guildPermissionFind -> {


                if(guildPermissionFind.has(id) && this.permission.has(id)){
                    status.set(false);
                    return;
                }
                if(!guildPermissionFind.has(id) && this.permission.has(id)){
                    status.set(false);
                    return;
                }
                if(guildPermissionFind.has(id) && !this.permission.has(id)){
                    status.set(true);
                    return;
                }
                status.set(true);
            });
        });
        return status.get();
    }
}
