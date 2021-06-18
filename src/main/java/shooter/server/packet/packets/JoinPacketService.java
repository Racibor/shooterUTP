package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.server.packet.Packet;
import shooter.server.packet.Parser;

public class JoinPacketService implements PacketService {
	
	@Override
	public Packet fromByteBuffer(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		return new JoinPacket();
	}
	
	@Override
	public ByteBuffer toByteBuffer(Packet packet) {
		JoinPacket temp = (JoinPacket)packet;
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);
		byteBuffer.putInt(temp.type.code);
		byteBuffer.flip();
		temp.size = byteBuffer.limit();
		return byteBuffer;
	}

}
