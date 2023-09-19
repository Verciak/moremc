package net.moremc.api.nats;

import io.nats.client.Connection;
import io.nats.client.Options;
import net.moremc.api.API;
import net.moremc.api.nats.messenger.PacketMessengerHandler;
import net.moremc.api.nats.packet.Packet;
import net.moremc.api.nats.packet.server.callback.Callback;
import net.moremc.api.nats.packet.server.callback.CallbackPacket;
import org.reflections.Reflections;

import java.io.IOException;
import java.time.Duration;

public class NatsMessengerAPI {

    private final String[] channelListenName;
    private Connection connection;


    public NatsMessengerAPI(String[] channelListenName, String packageName) {
        this.channelListenName = channelListenName;
        try {
            Options.Builder optionsBuilder = new Options.Builder().server("nats://" + "83.168.106.24" + ":" + 4222)
                    .reconnectWait(Duration.ofSeconds(10L)).maxReconnects(1000)
                    .connectionTimeout(Duration.ofSeconds(10))
                    .bufferSize(200000);


            this.connection = io.nats.client.Nats.connect(optionsBuilder.build());

            try {
                for (Class<? extends PacketMessengerHandler> messengerClass : new Reflections(packageName).getSubTypesOf(PacketMessengerHandler.class)) {
                    this.addListen(messengerClass.newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void sendPacket(String channel, CallbackPacket callbackPacket, Callback callback) {
        this.sendPacket(channel, callbackPacket);
        API.getInstance().getCallbackFactory().addCallback(callbackPacket.getCallbackId(), callback);
    }

    public void sendPacket(String channel, Packet packet) {
        this.connection.publish(channel, NatsPacketSerializer.serialize(packet));
    }
    public void addListen(PacketMessengerHandler packet) {
        this.connection.createDispatcher().subscribe(packet.getChannelListenName(), packet);
    }

    public String[] getChannelListenName() {
        return channelListenName;
    }
}
