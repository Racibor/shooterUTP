package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;
import shooter.server.packet.Parser;

public class LeavePacketService implements PacketService {

	
	@Override
	public Packet fromByteBuffer(ByteBuffer buffer) {
		int id = buffer.getInt();
		return new LeavePacket(id);
	}
	
	@Override
	public ByteBuffer toByteBuffer(Packet packet) {
		LeavePacket temp = (LeavePacket)packet;
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byteBuffer.putInt(temp.type.code);
		byteBuffer.putInt(temp.id);
		byteBuffer.flip();
		temp.size = byteBuffer.limit();
		return byteBuffer;
	}

}
