package net.moremc.api.entity.guild.regeneration;

import net.moremc.api.bukkit.BlockState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuildRegeneration implements Serializable {

    private final List<BlockState> blockStateList = new ArrayList<>();
    private GuildRegenerationType regenerationType = GuildRegenerationType.END;
    private int blocksPlace = 0;
    private long timeLeft = 0L;

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getBlocksPlace() {
        return blocksPlace;
    }

    public void setBlocksPlace(int blocksPlace) {
        this.blocksPlace = blocksPlace;
    }

    public GuildRegenerationType getRegenerationType() {
        return regenerationType;
    }

    public void setRegenerationType(GuildRegenerationType regenerationType) {
        this.regenerationType = regenerationType;
    }

    public List<BlockState> getBlockStateList() {
        return blockStateList;
    }

}
