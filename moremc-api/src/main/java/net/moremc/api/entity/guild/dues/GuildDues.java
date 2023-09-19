package net.moremc.api.entity.guild.dues;

import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.entity.guild.Guild;
import net.moremc.api.entity.guild.dues.category.GuildDuesCategory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GuildDues implements Serializable {

    private final int id;
    private final int inventorySlot;
    private String owner;
    private int amountWithdraw;
    private int amountEnd;
    private final Set<String> paymentPlayers;
    private String materialName;
    private boolean active;
    private long timeStart;
    private GuildDuesCategory category;

    public GuildDues(int id, int inventorySlot){
        this.id = id;
        this.inventorySlot = inventorySlot;
        this.owner = "null";
        this.amountWithdraw = 0;
        this.amountEnd = 0;
        this.paymentPlayers = new HashSet<>();
        this.materialName = "null";
        this.timeStart = 0L;
        this.active = false;
        this.category = GuildDuesCategory.WAITING;
    }


    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(Guild guild,String owner) {
        this.owner = owner;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public boolean hasActive(){
        return this.active;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public GuildDuesCategory getCategory() {
        return category;
    }

    public void setCategory(Guild guild, GuildDuesCategory category) {
        this.category = category;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public void setActive(Guild guild, boolean active) {
        this.active = active;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public boolean isActive() {
        return active;
    }

    public void setTimeStart(Guild guild, long timeStart) {
        this.timeStart = timeStart;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public void setAmountWithdraw(Guild guild, int amountProgress) {
        this.amountWithdraw = amountProgress;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public void setAmountEnd(Guild guild, int amountEnd) {
        this.amountEnd = amountEnd;
        guild.synchronize(SynchronizeType.UPDATE);
    }

    public void setMaterialName(Guild guild,String materialName) {
        this.materialName = materialName;
        guild.synchronize(SynchronizeType.UPDATE);
    }
    public void addPlayerPayment(Guild guild, String nickName){
        if(!this.paymentPlayers.contains(nickName)) {
            this.paymentPlayers.add(nickName);
            guild.synchronize(SynchronizeType.UPDATE);
        }
    }

    public int getId() {
        return id;
    }


    public int getAmountWithdraw() {
        return amountWithdraw;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getAmountEnd() {
        return amountEnd;
    }

    public Set<String> getPaymentPlayers() {
        return paymentPlayers;
    }
}
