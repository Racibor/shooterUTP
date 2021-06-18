package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

public class LeavePacket extends Packet {
	
	public int id;
	
	public LeavePacket() {
		type = ServerPacketType.LEAVE;
	}
	
	public LeavePacket(int id) {
		type = ServerPacketType.LEAVE;
		this.id = id;
	}
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeavePacket other = (LeavePacket) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
}
