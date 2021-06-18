package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;


public class DeadPacketService implements PacketService{

	
	public Packet fromByteBuffer(ByteBuffer buffer) {
		int id = buffer.getInt();
		return new DeadPacket(id);
	}

	
	public ByteBuffer toByteBuffer(Packet packet) {
		DeadPacket temp = (DeadPacket)packet;
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byteBuffer.putInt(temp.type.code);
		byteBuffer.putInt(temp.id);
		byteBuffer.flip();
		temp.size = byteBuffer.limit();
		return byteBuffer;
	}

}
