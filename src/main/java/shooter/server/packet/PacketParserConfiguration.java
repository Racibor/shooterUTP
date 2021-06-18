package shooter.server.packet;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;

import shooter.server.packet.packets.DeadPacketService;
import shooter.server.packet.packets.HitPacketService;
import shooter.server.packet.packets.InitPacketService;
import shooter.server.packet.packets.JoinPacketService;
import shooter.server.packet.packets.LeavePacketService;
import shooter.server.packet.packets.MovementPacketService;
import shooter.server.packet.packets.ShootPacketService;
import shooter.server.packet.packets.SpawnPacketService;

public class PacketParserConfiguration {
	
	Map<Integer, Parser<Packet, ByteBuffer>> handlers;
	
	Map<Integer, Parser<ByteBuffer, Packet>> parsers;
	
	public PacketParserConfiguration() {
		// TODO Auto-generated constructor stub
		handlers = new TreeMap<>();
		parsers = new TreeMap<>();
	}
	
	
	public PacketParserConfiguration registerHandler(ServerPacketType type, Parser<Packet, ByteBuffer> handler) {
		handlers.put(type.code, handler);
		return this;
	}
	
	public PacketParserConfiguration registerParser(ServerPacketType type, Parser<ByteBuffer, Packet> parser) {
		parsers.put(type.code, parser);
		return this;
	}
	
	public static PacketParserConfiguration getDefaultConfiguration() {
		MovementPacketService movementPacketService = new MovementPacketService();
		ShootPacketService shootPacketService = new ShootPacketService();
		JoinPacketService joinPacketService = new JoinPacketService();
		LeavePacketService leavePacketService = new LeavePacketService();
		InitPacketService initPacketService = new InitPacketService();
		SpawnPacketService spawnPacketService = new SpawnPacketService();
		HitPacketService hitPacketService = new HitPacketService();
		DeadPacketService deadPacketService = new DeadPacketService();
		
		PacketParserConfiguration config = new PacketParserConfiguration();
		config
		.registerHandler(ServerPacketType.MOVE, movementPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.MOVE, movementPacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.SHOOT, shootPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.SHOOT, shootPacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.JOIN, joinPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.JOIN, joinPacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.LEAVE, leavePacketService::fromByteBuffer)
		.registerParser(ServerPacketType.LEAVE, leavePacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.INITIAL, initPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.INITIAL, initPacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.SPAWN, spawnPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.SPAWN, spawnPacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.HIT, hitPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.HIT, hitPacketService::toByteBuffer)
		
		.registerHandler(ServerPacketType.DEAD, deadPacketService::fromByteBuffer)
		.registerParser(ServerPacketType.DEAD, deadPacketService::toByteBuffer);
		return config;
	}
}
