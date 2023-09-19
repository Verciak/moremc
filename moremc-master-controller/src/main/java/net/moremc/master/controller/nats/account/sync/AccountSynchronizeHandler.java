package net.moremc.master.controller.nats.account.sync;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.account.Account;
import net.moremc.api.entity.repository.AccountRepository;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.account.sync.AccountSynchronizePacket;
import net.moremc.api.service.entity.AccountService;

public class AccountSynchronizeHandler extends PacketMessengerHandler<AccountSynchronizePacket> {

    private final AccountService accountService = API.getInstance().getAccountService();
    private final AccountRepository accountRepository = API.getInstance().getAccountRepository();


    public AccountSynchronizeHandler() {
        super(AccountSynchronizePacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(AccountSynchronizePacket packet) {
        switch (packet.getUpdateType()){
            case CREATE: {
                if(this.accountService.findByValueOptional(packet.getNickName()).isPresent())return;
                Account accountPacket = new Gson().fromJson(packet.getSerializedAccount(), Account.class);
                this.accountService.getMap().put(accountPacket.getNickName(), accountPacket);
                this.accountRepository.create(accountPacket.getNickName(), accountPacket);
                API.getInstance().getNatsMessengerAPI().sendPacket("moremc_proxies_auth_channel" ,new AccountSynchronizePacket(accountPacket.getNickName(), new Gson().toJson(accountPacket), packet.getUpdateType()));
                break;
            }
            case UPDATE:{
                this.accountService.findByValueOptional(packet.getNickName()).ifPresent(account -> {
                    Account accountPacket = new Gson().fromJson(packet.getSerializedAccount(), Account.class);
                    this.accountService.getMap().replace(account.getNickName(), account, accountPacket);
                    this.accountRepository.update(accountPacket.getNickName(), accountPacket);
                     API.getInstance().getNatsMessengerAPI().sendPacket("moremc_proxies_auth_channel", new AccountSynchronizePacket(accountPacket.getNickName(), new Gson().toJson(accountPacket), packet.getUpdateType()));
                });
                break;
            }
            case REMOVE:{
                this.accountService.findByValueOptional(packet.getNickName()).ifPresent(account -> {
                    this.accountService.getMap().remove(account.getNickName(), account);
                    this.accountRepository.delete(account.getNickName(), account);
                    API.getInstance().getNatsMessengerAPI().sendPacket("moremc_proxies_auth_channel", new AccountSynchronizePacket(account.getNickName(), new Gson().toJson(account), packet.getUpdateType()));
                });
                break;
            }
        }
    }
}
