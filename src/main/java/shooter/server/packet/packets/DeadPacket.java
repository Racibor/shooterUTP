package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;


@DataPacket(type = ServerPacketType.DEAD)
public class DeadPacket extends Packet {
	
	public int id;
	
	public DeadPacket() {
		type = ServerPacketType.DEAD;
	}
	
	public DeadPacket(int id) {
		type = ServerPacketType.DEAD;
		this.id = id;
	}

	
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeadPacket other = (DeadPacket) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
