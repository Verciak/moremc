package net.moremc.api.entity.user;

import net.moremc.api.entity.user.type.UserSettingMessageType;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;

import java.io.Serializable;

public class UserSettingMessage implements Serializable {


    private final UserSettingMessageType settingMessageType;
    private final String messagePolishName;
    private final int slot;
    private boolean status;


    public UserSettingMessage(UserSettingMessageType settingMessageType, String messagePolishName, int slot){
        this.settingMessageType = settingMessageType;
        this.slot = slot;
        this.messagePolishName = messagePolishName;
        this.status = true;
    }

    public int getSlot() {
        return slot;
    }

    public void setStatus(User user, boolean status) {
        this.status = status;
        API.getInstance().getUserService().synchronizeUser(SynchronizeType.UPDATE, user);
    }

    public String getMessagePolishName() {
        return messagePolishName;
    }

    public UserSettingMessageType getSettingMessageType() {
        return settingMessageType;
    }

    public boolean isStatus() {
        return status;
    }
}