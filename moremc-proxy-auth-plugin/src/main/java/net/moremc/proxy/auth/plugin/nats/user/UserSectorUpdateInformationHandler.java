package net.moremc.proxy.auth.plugin.nats.user;

import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.type.SynchronizeType;
import net.moremc.api.nats.packet.user.UserSectorUpdateInformationPacket;
import net.moremc.api.service.entity.AccountService;

public class UserSectorUpdateInformationHandler extends PacketMessengerHandler<UserSectorUpdateInformationPacket> {


    private final AccountService accountService = API.getInstance().getAccountService();

    public UserSectorUpdateInformationHandler() {
        super(UserSectorUpdateInformationPacket.class, "moremc_proxies_auth_channel");
    }

    @Override
    public void onHandle(UserSectorUpdateInformationPacket packet) {
        this.accountService.findByValueOptional(packet.getNickName()).ifPresent(account -> {
            account.setActualSectorName(packet.getSectorName());
            account.synchronize(SynchronizeType.UPDATE);
        });
    }
}
