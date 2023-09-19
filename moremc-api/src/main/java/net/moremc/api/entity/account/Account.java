package net.moremc.api.entity.account;

import com.google.gson.Gson;
import net.moremc.api.entity.account.state.AccountState;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.API;
import net.moremc.api.helper.RandomStringHelper;
import net.moremc.api.mysql.Identifiable;
import net.moremc.api.nats.packet.account.sync.AccountSynchronizePacket;

import java.io.Serializable;
import java.util.UUID;

public class Account implements Serializable, Identifiable<String> {

    private UUID uuid;
    private final String nickName;
    private final String addressName;
    private String password;
    private String captcha;
    private boolean premium;
    private boolean login;
    private String actualSectorName;
    private final AccountProfileSkin profileSkin;
    private AccountState state;


    public Account(String nickName, String addressName) {
        this.nickName = nickName;
        this.addressName = addressName;
        this.password = null;
        this.premium = false;
        this.uuid = null;
        this.captcha = "null";
        this.actualSectorName = "spawn01";
        this.profileSkin = new AccountProfileSkin();
        this.state = AccountState.REGISTER;
        this.generateCaptcha();
        this.synchronize(SynchronizeType.CREATE);
    }
    public void synchronize(SynchronizeType synchronizeType){
        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_master_controller", new AccountSynchronizePacket(this.nickName, new Gson().toJson(this), synchronizeType));
    }

    public String getCaptcha() {
        return captcha;
    }

    public AccountProfileSkin getProfileSkin() {
        return profileSkin;
    }

    public String getNickName() {
        return nickName;
    }

    public AccountState getState() {
        return state;
    }

    public boolean isPremium() {
        return premium;
    }

    public String getPassword() {
        return password;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setPassword(String password) {
        this.password = password;
        this.synchronize(SynchronizeType.UPDATE);
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
        this.synchronize(SynchronizeType.UPDATE);
    }

    public void setState(AccountState state) {
        this.state = state;
        this.synchronize(SynchronizeType.UPDATE);
    }

    @Override
    public String getID() {
        return this.nickName;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
        this.synchronize(SynchronizeType.UPDATE);
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        this.synchronize(SynchronizeType.UPDATE);
    }
    public void generateCaptcha() {
        if (this.captcha.equals("null")) {
            this.captcha = RandomStringHelper.getString(5);
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid=" + uuid +
                ", nickName='" + nickName + '\'' +
                ", addressName='" + addressName + '\'' +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                ", premium=" + premium +
                ", login=" + login +
                ", profileSkin=" + profileSkin +
                ", state=" + state +
                '}';
    }

    public String getActualSectorName() {
        return actualSectorName;
    }

    public void setActualSectorName(String actualSectorName) {
        this.actualSectorName = actualSectorName;
    }
}
