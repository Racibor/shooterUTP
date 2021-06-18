package shooter.server.packet;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import shooter.server.packet.packets.JoinPacket;
import shooter.server.packet.packets.MovementPacket;
import shooter.server.packet.packets.SpawnPacket;

public class PacketParser {
	
	private Map<Integer, Parser<Packet, ByteBuffer>> handlers;
	
	private Map<Integer, Parser<ByteBuffer, Packet>> parsers;
	
	
	public PacketParser(PacketParserConfiguration config) {
		handlers = config.handlers;
		parsers = config.parsers;
	}
	
	public PacketGroup getPacketsFromBuffer(ByteBuffer data) {
		long tick = data.getLong();
		PacketGroup packetGroup = new PacketGroup(tick);
		int size = data.getInt();
		
		for(int i=0; i<size; i++) {
			int type = data.getInt();
			ServerPacketType packetType = ServerPacketType.valueOf(type);
			Parser<Packet, ByteBuffer> handler = handlers.get(packetType.code);
			Packet packet = handler.parse(data);
			if(packet != null) {
				packetGroup.insert(packet);
			}
		}
		return packetGroup;
	}
	
	public ByteBuffer getPacketsAsBuffer(PacketGroup packetGroup) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		List<Packet> packetList = packetGroup.getPacketList();
		byteBuffer.putLong(packetGroup.tick);
		byteBuffer.putInt(packetList.size());
		for(Packet packet : packetList) {
			Parser<ByteBuffer, Packet> parser = parsers.get(packet.type.code);
			ByteBuffer buffer = parser.parse(packet);
			byteBuffer.put(buffer);
		}
		byteBuffer.flip();
		return byteBuffer;
	}

}
