package shooter.server.packet.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import shooter.game.entity.Entity;
import shooter.server.packet.Packet;
import shooter.server.packet.ServerPacketType;

public class InitialPacket extends Packet {
	
	public int id;
	
	public int seed;
	
	public int width = 1000;
	
	public int height = 500;
	
	public List<Entity> entityList;
	
	public InitialPacket() {
		type = ServerPacketType.INITIAL;
		entityList = new ArrayList<>();
	}
	
	public InitialPacket(int id, int seed, List<Entity> entityList) {
		type = ServerPacketType.INITIAL;
		this.id = id;
		this.seed = seed;
		this.entityList = entityList;
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
		InitialPacket other = (InitialPacket) obj;
		if (entityList == null) {
			if (other.entityList != null)
				return false;
		} else if (!entityList.equals(other.entityList))
			return false;
		if (height != other.height)
			return false;
		if (id != other.id)
			return false;
		if (seed != other.seed)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	
}
