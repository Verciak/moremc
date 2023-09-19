package net.moremc.api.entity.account;

import java.io.Serializable;

public class AccountProfileSkin implements Serializable {

    private String value;
    private String signature;

    public AccountProfileSkin(){
        this.value = null;
        this.signature = null;
    }

    public String getSignature() {
        return signature;
    }

    public String getValue() {
        return value;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AccountProfileSkin{" +
                "value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
