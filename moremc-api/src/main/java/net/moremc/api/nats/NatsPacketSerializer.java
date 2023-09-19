package net.moremc.api.nats;

import net.moremc.api.nats.packet.Packet;
import org.nustaq.serialization.FSTConfiguration;

public final class NatsPacketSerializer {

  private static final FSTConfiguration FST_CONFIGURATION = FSTConfiguration.createDefaultConfiguration();

  private NatsPacketSerializer() {
  }

  public static Packet deserialize(byte[] input) {
    return (Packet) FST_CONFIGURATION.asObject(input);
  }

  public static byte[] serialize(Packet packet) {
    return FST_CONFIGURATION.asByteArray(packet);
  }
}
