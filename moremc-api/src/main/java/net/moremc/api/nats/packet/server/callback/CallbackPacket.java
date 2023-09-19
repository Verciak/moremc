package net.moremc.api.nats.packet.server.callback;

import net.moremc.api.nats.packet.Packet;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public abstract class CallbackPacket implements Packet<CallbackPacket>, Serializable {

    private long callbackId = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
    private boolean response, success;
    private String responseText;

    public long getCallbackId() {
        return callbackId;
    }

    public String getResponseText() {
        return responseText;
    }

    public boolean isResponse() {
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCallbackId(long callbackId) {
        this.callbackId = callbackId;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

