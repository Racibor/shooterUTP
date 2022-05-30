package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

@DataPacket(type = ServerPacketType.MOVE)
public class MovementPacket extends Packet {
	
	public int id;
	
	public float x; 
	
	public float y; 
	
	public float angle;
	
	public MovementPacket() {
		type = ServerPacketType.MOVE;
	}
	
	public MovementPacket(int id, float x, float y, float angle) {
		type = ServerPacketType.MOVE;
		this.id = id;
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovementPacket other = (MovementPacket) obj;
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
