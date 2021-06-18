package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;

public class HitPacketService implements PacketService {
	
	@Override
	public Packet fromByteBuffer(ByteBuffer buffer) {
		int hittingId = buffer.getInt();
		int hittedId = buffer.getInt();
		int newHp = buffer.getInt();
		HitPacket movementPacket = new HitPacket(hittingId, hittedId, newHp);
		return movementPacket;
	}
	
	@Override
	public ByteBuffer toByteBuffer(Packet packet) {
		HitPacket temp = (HitPacket)packet;
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byteBuffer.putInt(temp.type.code);
		byteBuffer.putInt(temp.hittingId);
		byteBuffer.putInt(temp.hittedId);
		byteBuffer.putInt(temp.newHp);
		byteBuffer.flip();
		temp.size = byteBuffer.limit();
		return byteBuffer;
	}

}
