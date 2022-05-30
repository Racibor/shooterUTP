package shooter.server.packet.packets;

import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

@DataPacket(type = ServerPacketType.SPAWN)
public class SpawnPacket extends Packet {
	
	public int id;
	
	public float x;
	
	public float y;
	
	public float angle;
	
	public boolean isPlayer;
	
	public SpawnPacket() {
		type = ServerPacketType.SPAWN;
	}
	
	public SpawnPacket(int id, float x, float y, float angle, boolean isPlayer) {
		type = ServerPacketType.SPAWN;
		this.id = id;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.isPlayer = isPlayer;
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
		SpawnPacket other = (SpawnPacket) obj;
		if (Float.floatToIntBits(angle) != Float.floatToIntBits(other.angle))
			return false;
		if (id != other.id)
			return false;
		if (isPlayer != other.isPlayer)
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

}
