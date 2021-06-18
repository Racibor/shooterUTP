package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

public class ShootPacket extends Packet {
	
	public int id;
	
	public float x;
	
	public float y;
	
	public float angle;
	
	public ShootPacket() {
		type = ServerPacketType.SHOOT;
	}
	
	public ShootPacket(int id, float x, float y, float angle) {
		type = ServerPacketType.SHOOT;
		this.id = id;
		this.x = x;
		this.y = y;
		this.angle = angle;
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
		ShootPacket other = (ShootPacket) obj;
		if (Float.floatToIntBits(angle) != Float.floatToIntBits(other.angle))
			return false;
		if (id != other.id)
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

}
