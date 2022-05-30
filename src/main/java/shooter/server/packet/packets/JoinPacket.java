package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

@DataPacket(type = ServerPacketType.JOIN)
public class JoinPacket extends Packet {
	
	public JoinPacket() {
		type = ServerPacketType.JOIN;
	}
	
	public JoinPacket(String name) {
		type = ServerPacketType.JOIN;

	}
	
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
}
