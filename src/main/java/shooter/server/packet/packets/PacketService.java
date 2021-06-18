package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;
import shooter.server.packet.Parser;

public interface PacketService {
	
	public Packet fromByteBuffer(ByteBuffer buffer);
	
	public ByteBuffer toByteBuffer(Packet packet);
}
