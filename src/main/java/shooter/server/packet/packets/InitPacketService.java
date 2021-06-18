package shooter.server.packet.packets;

import java.nio.ByteBuffer;

import shooter.game.entity.Entity;
import shooter.game.entity.Player;
import shooter.game.entity.Position;
import shooter.server.packet.Packet;
import shooter.server.packet.Parser;

public class InitPacketService implements PacketService{
	
	@Override
	public Packet fromByteBuffer(ByteBuffer buffer) {
		InitialPacket init = new InitialPacket();
		init.id = buffer.getInt();
		init.seed = buffer.getInt();
		init.width = buffer.getInt();
		init.height = buffer.getInt();
		int size = buffer.getInt();
		for(int i=0; i<size; i++) {
			int id = buffer.getInt();
			int nameSize = buffer.getInt();
			byte[] nameBuffer = new byte[nameSize];
			buffer.get(nameBuffer, 0, nameSize);
			boolean isAlive = buffer.get() == 1 ? true : false;
			boolean isPlayer = buffer.get() == 1 ? true : false;
			Position pos = new Position(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
			Entity entity;
			if(isPlayer) {
				entity = new Player(new String(nameBuffer), pos);
				entity.setAlive(isAlive);
			} else {
				entity = new Entity(new String(nameBuffer), pos);
				entity.setAlive(isAlive);
			}
			entity.setId(id);
			init.entityList.add(entity);
		}
		return init;
	}
	
	@Override
	public ByteBuffer toByteBuffer(Packet packet) {
			InitialPacket temp = (InitialPacket)packet;
			ByteBuffer byteBuffer = ByteBuffer.allocate(256);
			byteBuffer.putInt(temp.type.code);
			byteBuffer.putInt(temp.id);
			byteBuffer.putInt(temp.seed);
			byteBuffer.putInt(temp.width);
			byteBuffer.putInt(temp.height);
			byteBuffer.putInt(temp.entityList.size());
			int tmp = temp.entityList.size();
			for(Entity entity : temp.entityList) {
				byteBuffer.putInt(entity.getId());
				byte[] name = entity.getName().getBytes();
				byteBuffer.putInt(name.length);
				byteBuffer.put(name);
				byteBuffer.put(entity.isAlive() ? (byte)1 : (byte)0);
				byteBuffer.put(entity.isPlayer() ? (byte)1 : (byte)0);
				Position pos = entity.getPosition();
				byteBuffer.putFloat(pos.getX());
				byteBuffer.putFloat(pos.getY());
				byteBuffer.putFloat(pos.getAngle());
			}
			byteBuffer.flip();
			temp.size = byteBuffer.limit();
			return byteBuffer;
	}

	

}
