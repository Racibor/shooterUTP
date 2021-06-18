package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;
import shooter.server.packet.Parser;

public class ShootPacketService implements PacketService {
	
	@Override
	public Packet fromByteBuffer(ByteBuffer buffer) {
		int id = buffer.getInt();
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		float angle = buffer.getFloat();
		ShootPacket movementPacket = new ShootPacket(id, x, y, angle);
		return movementPacket;
	}
	
	@Override
	public ByteBuffer toByteBuffer(Packet packet) {
		ShootPacket temp = (ShootPacket)packet;
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byteBuffer.putInt(temp.type.code);
		byteBuffer.putInt(temp.id);
		byteBuffer.putFloat(temp.x);
		byteBuffer.putFloat(temp.y);
		byteBuffer.putFloat(temp.angle);
		byteBuffer.flip();
		temp.size = byteBuffer.limit();
		return byteBuffer;
	}

}
