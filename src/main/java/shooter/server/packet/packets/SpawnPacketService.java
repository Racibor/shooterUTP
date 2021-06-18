package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;
import shooter.server.packet.Parser;

public class SpawnPacketService implements PacketService {
	
	@Override
	public Packet fromByteBuffer(ByteBuffer buffer) {
		int id = buffer.getInt();
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		float angle = buffer.getFloat();
		boolean isPlayer = buffer.get() == 1 ? true : false;
		SpawnPacket movementPacket = new SpawnPacket(id, x, y, angle, isPlayer);
		return movementPacket;
	}
	
	@Override
	public ByteBuffer toByteBuffer(Packet packet) {
		SpawnPacket temp = (SpawnPacket)packet;
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byteBuffer.putInt(temp.type.code);
		byteBuffer.putInt(temp.id);
		byteBuffer.putFloat(temp.x);
		byteBuffer.putFloat(temp.y);
		byteBuffer.putFloat(temp.angle);
		byteBuffer.put(temp.isPlayer ? (byte)1 : (byte)0);
		byteBuffer.flip();
		temp.size = byteBuffer.limit();
		return byteBuffer;
	}

}
