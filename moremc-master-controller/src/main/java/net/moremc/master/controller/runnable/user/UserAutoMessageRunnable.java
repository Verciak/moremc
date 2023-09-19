package net.moremc.master.controller.runnable.user;

import net.moremc.api.API;
import net.moremc.api.nats.packet.client.ClientSendMessagePacket;
import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserAutoMessageRunnable implements Runnable{

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    private final List<String> messageList = Arrays.asList(
            "&8[&5&lPAY&f&lMC.PL&8] &5>> &fDołącz na naszego discorda&8: &ddc.paymc.pl",
            "&8[&5&lPAY&f&lMC.PL&8] &5>> &fNasza strona internetowa&8: &dhttps://www.paymc.pl",
            "&8[&5&lPAY&f&lMC.PL&8] &5>> &fZbyt dużo ludzi na spawn? &fużyj komendy&8: &d/ch",
            "&8[&5&lPAY&f&lMC.PL&8] &5>> &fZakupiłeś przedmioty i nie wiesz jak odebrać? &d/odbierz");
    private int index = 0;


    public void start(){
        this.scheduledExecutorService.scheduleAtFixedRate(this, 1L, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {

        if (this.index >= this.messageList.size()) {
            this.index = 0;
        }
        ClientSendMessagePacket sendMessagePacket = new ClientSendMessagePacket(this.messageList.get(index),
                SendMessageReceiverType.ALL, SendMessageType.CHAT);

        API.getInstance().getNatsMessengerAPI().sendPacket("moremc_client_channel", sendMessagePacket);

        this.index++;

    }
}
