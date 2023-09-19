package net.moremc.master.controller.nats.account.request;

import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.account.request.AccountLoadRequestPacket;
import net.moremc.api.nats.packet.account.response.AccountLoadResponsePacket;
import net.moremc.api.service.entity.AccountService;

import java.util.ArrayList;

public class AccountLoadRequestHandler extends PacketMessengerHandler<AccountLoadRequestPacket> {


    private final AccountService accountService = API.getInstance().getAccountService();


    public AccountLoadRequestHandler() {
        super(AccountLoadRequestPacket.class, "moremc_master_controller");
    }

    @Override
    public void onHandle(AccountLoadRequestPacket packet) {
        API.getInstance().getNatsMessengerAPI().sendPacket(packet.getProxySender() , new AccountLoadResponsePacket(new Gson().toJson(new ArrayList<>(this.accountService.getMap().values()))));
    }
}
