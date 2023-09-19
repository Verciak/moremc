package net.moremc.api.nats.messenger;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import net.moremc.api.API;
import net.moremc.api.nats.NatsPacketSerializer;
import net.moremc.api.nats.packet.Packet;
import net.moremc.api.nats.packet.server.callback.Callback;
import net.moremc.api.nats.packet.server.callback.CallbackPacket;

import java.util.Objects;

public abstract class PacketMessengerHandler<T extends Packet<?>> implements MessageHandler{

    private final Class<T> packetClass;
    private final String channelListenName;

    public abstract void onHandle(T packet);

    public PacketMessengerHandler(Class<T> packetClass, String channelListenName){
        this.packetClass = packetClass;
        this.channelListenName = channelListenName;
    }

    @Override
    public void onMessage(Message message) {
        if(API.getInstance().getNatsMessengerAPI() == null)return;
        Packet packet = NatsPacketSerializer.deserialize(message.getData());

        if(packet.getClass().isAssignableFrom(this.packetClass)) {

            if (packet instanceof CallbackPacket) {
                CallbackPacket callbackPacket = (CallbackPacket) packet;
                if (!callbackPacket.isResponse()) {
                    this.onHandle((T) packet);
                    return;
                }
                Callback callback = API.getInstance().getCallbackFactory().findCallbackById(callbackPacket.getCallbackId());
                if (Objects.isNull(callback)) {
                    return;
                }
                if (!callbackPacket.isSuccess()) {
                    callback.done(packet);
                    System.out.println("Done: " + packet.getClass().getSimpleName());
                    return;
                }
                callback.error(callbackPacket.getResponseText());
                return;
            }
            this.onHandle((T) packet);
        }
    }

    public String getChannelListenName() {
        return channelListenName;
    }
}
