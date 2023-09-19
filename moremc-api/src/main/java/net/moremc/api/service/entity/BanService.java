package net.moremc.api.service.entity;

import net.moremc.api.entity.ban.Ban;
import net.moremc.api.service.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class BanService extends ServiceImpl<String, Ban>
{

    private  List<Ban> banList = new ArrayList<>();

    public Ban create(String nickName, String adminNickName, String addressHostName, String reason, long tempDelay){
        return new Ban(nickName, adminNickName, addressHostName, reason, tempDelay);
    }

    public Ban findBanByNickName(String nickName){
        return this.banList
                .stream()
                .filter(ban -> ban.getNickName().equalsIgnoreCase(nickName))
                .findFirst()
                .orElse(null);
    }
    public Ban findBanByHostName(String hostName){
        return this.banList
                .stream()
                .filter(ban -> ban.getAddressHostName().equalsIgnoreCase(hostName))
                .findFirst()
                .orElse(null);
    }

    public List<Ban> getBanList() {
        return banList;
    }

    public void setBanList(List<Ban> banList) {
        this.banList = banList;
    }
}
