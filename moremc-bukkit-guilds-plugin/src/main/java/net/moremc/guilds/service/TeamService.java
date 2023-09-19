
package net.moremc.guilds.service;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.moremc.api.API;
import net.moremc.api.entity.guild.Guild;
import net.moremc.api.entity.guild.impl.GuildImpl;
import net.moremc.api.entity.user.User;
import net.moremc.api.service.entity.GuildService;
import net.moremc.bukkit.api.helper.MessageHelper;

import java.util.Optional;

public class TeamService {


    private final Scoreboard scoreboard;
    private final GuildService guildService = API.getInstance().getGuildService();

    public TeamService() {
        this.scoreboard = new Scoreboard();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void initializeNameTag(Player player, User user) {

        ScoreboardTeam scoreboardPlayerTeam = this.scoreboard.getPlayerTeam(user.getFakeNickname());
        if (scoreboardPlayerTeam == null){
            scoreboardPlayerTeam = this.scoreboard.createTeam(user.getFakeNickname());
        }
        if (!scoreboardPlayerTeam.getPlayerNameSet().contains(user.getFakeNickname())) {
            this.scoreboard.addPlayerToTeam(user.getFakeNickname(), scoreboardPlayerTeam.getName());
            CraftPlayer craftPlayer = (CraftPlayer) player;
            if(user.isIncognito()) scoreboardPlayerTeam.setSuffix("");
            if(user.hasProtection())scoreboardPlayerTeam.setSuffix(" " + MessageHelper.translateText("&5&lOCHRONA"));
            scoreboardPlayerTeam.setSuffix(" " + MessageHelper.translateText(user.getGroupType().name().equals("PLAYER") ? "" : user.getGroupType().getPrefix()));
            PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam(scoreboardPlayerTeam, 0);
            craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutScoreboardTeam);

            for (Player playerTo : Bukkit.getOnlinePlayers()) {
                if(playerTo.equals(player))continue;

                ScoreboardTeam finalScoreboardPlayerTeam = scoreboardPlayerTeam;
                API.getInstance().getUserService().findUserByUUID(playerTo.getUniqueId()).ifPresent(userTo -> {
                    ((CraftPlayer) playerTo).getHandle().playerConnection.sendPacket(packetPlayOutScoreboardTeam);
                    ScoreboardTeam playerToScoreBoardTeam = this.scoreboard.getPlayerTeam(userTo.getFakeNickname());
                    if (playerToScoreBoardTeam == null) return;
                    if (user.isIncognito()) finalScoreboardPlayerTeam.setSuffix("");
                    if (user.hasProtection())
                        finalScoreboardPlayerTeam.setSuffix(" " + MessageHelper.translateText("&5&lOCHRONA"));
                    finalScoreboardPlayerTeam.setSuffix(" " + MessageHelper.translateText(user.getGroupType().name().equals("PLAYER") ? "" : user.getGroupType().getPrefix()));
                    craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(playerToScoreBoardTeam, 0));
                });
            }
        }
    }
    public void updateBoard(Optional<GuildImpl> team, User user) {
        ScoreboardTeam scoreboardTeam = this.scoreboard.getPlayerTeam(user.getFakeNickname());
        if (scoreboardTeam == null) return;
        scoreboardTeam.setSuffix(" " + MessageHelper.translateText(user.getGroupType().name().equals("PLAYER") ? "" : user.getGroupType().getPrefix()));
        if (user.isIncognito()) scoreboardTeam.setSuffix("");
        if (user.hasProtection())
            scoreboardTeam.setSuffix(" " + MessageHelper.translateText("&5&lOCHRONA"));

        for (Player players : Bukkit.getOnlinePlayers()) {
            Guild guild = null;
            Optional<GuildImpl> guildOptional = this.guildService.findGuildByNickName(players.getName());

            if (guildOptional.isPresent()) {
                guild = guildOptional.get();
            }

            Guild finalGuild = guild;
            API.getInstance().getUserService().findUserByUUID(players.getUniqueId()).ifPresent(userTo -> {

                if(user.isIncognito()){
                    scoreboardTeam.setSuffix(MessageHelper.translateText("&c◉"));
                }

                if (team.isPresent() && team.get().equals(finalGuild)) {
                    team.ifPresent(guildFind -> {
                        scoreboardTeam.setPrefix(MessageHelper.translateText("&2[&a" + guildFind.getName().toUpperCase() + "&2]&a "));
                    });
                } else if (team.isPresent() && !team.get().equals(finalGuild)) {
                    team.ifPresent(guildFind -> {
                        scoreboardTeam.setPrefix(MessageHelper.translateText("&4[&c" + guildFind.getName().toUpperCase() + "&4]&c "));
                        if (user.isIncognito()) {
                            scoreboardTeam.setPrefix(MessageHelper.translateText("&4[&c?&4] &c"));
                        }
                    });
                }
                if (!team.isPresent()) {
                    scoreboardTeam.setPrefix(MessageHelper.translateText("&7"));
                }
                send(scoreboardTeam, players);
            });
        }
    }
    public void removeNameTag(final Player player, User user) {
        ScoreboardTeam scoreboardPlayerTeam = this.scoreboard.getPlayerTeam(user.getFakeNickname());
        if (scoreboardPlayerTeam == null) return;


        this.scoreboard.removePlayerFromTeam(user.getFakeNickname(), scoreboardPlayerTeam);
        scoreboardPlayerTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);

        CraftPlayer craftPlayer = (CraftPlayer) player;
        final PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam(scoreboardPlayerTeam, 1);
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutScoreboardTeam);
        for (Player playerTo : Bukkit.getOnlinePlayers()) {
            if (playerTo.equals(player) || playerTo.hasMetadata("NPC")) continue;
            ((CraftPlayer) playerTo).getHandle().playerConnection.sendPacket(packetPlayOutScoreboardTeam);
            API.getInstance().getUserService().findUserByUUID(playerTo.getUniqueId()).ifPresent(userTo -> {
               ScoreboardTeam team = this.scoreboard.getTeam(userTo.getFakeNickname());
               if (team == null) return;
               craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
            });
        }
        this.scoreboard.removeTeam(scoreboardPlayerTeam);
    }
    public void send(ScoreboardTeam scoreboardTeam, Player player){
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(scoreboardTeam, 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}