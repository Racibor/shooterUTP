package shooter.server.packet.packets;

import shooter.server.packet.ServerPacketType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DataPacket {
    public ServerPacketType type();
}
