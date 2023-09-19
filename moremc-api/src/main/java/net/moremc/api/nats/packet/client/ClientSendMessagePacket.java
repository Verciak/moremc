package net.moremc.api.nats.packet.client;

import net.moremc.api.nats.packet.client.type.SendMessageReceiverType;
import net.moremc.api.nats.packet.client.type.SendMessageType;
import net.moremc.api.nats.packet.server.InfoPacket;

public class ClientSendMessagePacket extends InfoPacket {

    private String nickNameTarget;
    private final String message;
    private final SendMessageReceiverType messageReceiverType;
    private final SendMessageType messageType;

    public ClientSendMessagePacket(String message, SendMessageReceiverType messageReceiverType, SendMessageType messageType){
        this.message = message;
        this.messageReceiverType = messageReceiverType;
        this.messageType = messageType;
    }

    public void setNickNameTarget(String nickNameTarget) {
        this.nickNameTarget = nickNameTarget;
    }

    public SendMessageReceiverType getMessageReceiverType() {
        return messageReceiverType;
    }

    public SendMessageType getMessageType() {
        return messageType;
    }

    public String getNickNameTarget() {
        return nickNameTarget;
    }

    public String getMessage() {
        return message;
    }
    @Override
    public String toString() {
        return "SendMessagePacket{" +
                "nickNameTarget='" + nickNameTarget + '\'' +
                ", message='" + message + '\'' +
                ", messageReceiverType=" + messageReceiverType +
                ", messageType=" + messageType +
                '}';
    }
}
