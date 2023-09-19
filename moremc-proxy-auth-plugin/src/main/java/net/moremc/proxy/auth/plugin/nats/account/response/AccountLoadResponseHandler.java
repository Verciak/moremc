package net.moremc.proxy.auth.plugin.nats.account.response;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.moremc.api.API;
import net.moremc.api.entity.account.Account;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.account.response.AccountLoadResponsePacket;
import net.moremc.api.service.entity.AccountService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AccountLoadResponseHandler extends PacketMessengerHandler<AccountLoadResponsePacket> {

    private final AccountService accountService = API.getInstance().getAccountService();

    public AccountLoadResponseHandler() {
        super(AccountLoadResponsePacket.class, "proxy01");
    }

    @Override
    public void onHandle(AccountLoadResponsePacket packet) {
        Type listOfMyClassObject = new TypeToken<ArrayList<Account>>(){}.getType();
        List<Account> accountList = new Gson().fromJson(packet.getSerializedList(), listOfMyClassObject);
        System.out.println("[MASTER-SERVER] Sent " + accountList.size() + " account.");
        accountList.forEach(account -> this.accountService.getMap().put(account.getNickName(), account));
    }
}
