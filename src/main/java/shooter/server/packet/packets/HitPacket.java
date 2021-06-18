package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

public class HitPacket extends Packet {
	
	public int hittingId;
	
	public int hittedId;
	
	public int newHp;
	
	public HitPacket() {
		type = ServerPacketType.HIT;
	}
	
	public HitPacket(int hittingId, int hittedId, int newHp) {
		type = ServerPacketType.HIT;
		this.hittingId = hittingId;
		this.hittedId = hittedId;
		this.newHp = newHp;
	}

	
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
		HitPacket other = (HitPacket) obj;
		if (hittedId != other.hittedId)
			return false;
		if (hittingId != other.hittingId)
			return false;
		if (newHp != other.newHp)
			return false;
		return true;
	}
	
	
}
