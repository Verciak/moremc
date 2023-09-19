package net.moremc.proxy.auth.plugin.nats.account.sync;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.account.Account;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.account.sync.AccountSynchronizePacket;
import net.moremc.api.service.entity.AccountService;

public class AccountSynchronizeHandler extends PacketMessengerHandler<AccountSynchronizePacket> {

    private final AccountService accountService = API.getInstance().getAccountService();


    public AccountSynchronizeHandler() {
        super(AccountSynchronizePacket.class, "moremc_proxies_auth_channel");
    }

    @Override
    public void onHandle(AccountSynchronizePacket packet) {
        switch (packet.getUpdateType()){
            case CREATE: {
                if(this.accountService.findByValueOptional(packet.getNickName()).isPresent())return;
                Account accountPacket = new Gson().fromJson(packet.getSerializedAccount(), Account.class);
                this.accountService.getMap().put(accountPacket.getNickName(), accountPacket);
                break;
            }
            case UPDATE:{
                this.accountService.findByValueOptional(packet.getNickName()).ifPresent(account -> {
                    Account accountPacket = new Gson().fromJson(packet.getSerializedAccount(), Account.class);
                    this.accountService.getMap().replace(account.getNickName(), account, accountPacket);
                });
                break;
            }
            case REMOVE:{
                this.accountService.findByValueOptional(packet.getNickName()).ifPresent(account -> {
                    this.accountService.getMap().remove(account.getNickName(), account);
                });
                break;
            }
        }
    }

}
