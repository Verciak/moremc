package net.moremc.api.entity.guild;

import net.moremc.api.entity.guild.player.GuildPlayer;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.serializer.LocationSerializer;

public interface Guild {


    /**
     * @parm Method setLocationHome, set home location after teleport
     */
    void setLocationHome(LocationSerializer location);

    /**
     * @parm Method addSize, add size terrain guild
     */
    void addSize(int size);

    /**
     * @parm Method addPlayer, add player to the guild members
     */
    void addPlayer(GuildPlayer player);

    /**
     * @parm Method removePlayer, remove player from the guild members
     */
    void removePlayer(GuildPlayer player);

    /**
     * @parm Method hasPlayer, check player exists in guild
     */
    GuildPlayer hasPlayer(String nickName);

    /**
     * @parm Method addAlly, add ally to the guild allyiases
     */
    void addAlly(GuildAlly ally);

    /**
     * @parm Method removeAlly, remove ally from the guild allyiases
     */
    void removeAlly(GuildAlly ally);


    /**
     * @parm Method hasAlly, check ally exists in guild allies
     */
    boolean checkAlly(String guildName);

    /**
     * @parm Method sendInviteAlly, send invite ally to the other guild
     */
    void sendInviteAlly(GuildAlly ally);

    /**
     * @parm Method removeInviteAlly, remove invite ally from guild invitesAlly
     */
    void removeInviteAlly(GuildAlly ally);

    /**
     * @parm Method hasInviteAlly, check invite ally in allyInvites
     */
    boolean hasInviteAlly(String guildName);

    /**
     * @parm Method addPoints, add points to guild
     */
    void addPoints(int points);

    /**
     * @parm Method removePoints, remove points from guild
     */
    void removePoints(int points);

    /**
     * @parm Method addKills, add kills to guild
     */
    void addKills(int kills);

    /**
     * @parm Method addDeaths, add deaths to guild
     */
    void addDeaths(int deaths);
    
    /**
     * @parm Method addWar, add war with other guild to guild#getWars();
     */
    void addWar(GuildWar war);

    /**
     * @parm Method removeWar, remove war with other guild from guild#getWars();
     */
    void removeWar(GuildWar war);

    /**
     * @parm Method hasGuildWar, check with other guild war from guild#getWars();
     */
    boolean hasGuildWar(String guildName);

    /**
     * @parm Method sendInviteWar, send invite war to other guild and add to guild#getWarInvites();
     */
    void sendInviteWar(GuildWar war);

    /**
     * @parm Method removeInviteWar, remove invite war with other guild and remove from guild#getWarInvites();
     */
    void removeInviteWar(GuildWar war);

    /**
     * @parm Method hasGuildWarInvite, check with other guild from guild#getWars();
     */
    boolean hasGuildWarInvite(String guildName);

    /**
     * @parm Method synchronize, synchronize guild object from the master-server
     */
    void synchronize(SynchronizeType type);

}
