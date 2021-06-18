package shooter.server.packet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shooter.game.entity.Entity;
import shooter.game.entity.Position;
import shooter.server.packet.packets.DeadPacket;
import shooter.server.packet.packets.HitPacket;
import shooter.server.packet.packets.InitialPacket;
import shooter.server.packet.packets.JoinPacket;
import shooter.server.packet.packets.LeavePacket;
import shooter.server.packet.packets.MovementPacket;
import shooter.server.packet.packets.ShootPacket;
import shooter.server.packet.packets.SpawnPacket;


public class PacketParserTest {
	PacketParser packetParser;
	
	public PacketParserTest() {
		// TODO Auto-generated constructor stub
		PacketParserConfiguration packetParserConfiguration = PacketParserConfiguration.getDefaultConfiguration();
		packetParser = new PacketParser(packetParserConfiguration);
	}
	
	@Test
	public void parserTest() {
		PacketGroup packetGroup = new PacketGroup(-1);
		packetGroup.insert(new MovementPacket(0, 1, 1, 1));
		packetGroup.insert(new DeadPacket(0));
		packetGroup.insert(new HitPacket(1, 2, 3));
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(new Entity("test", new Position(1, 1, 1)));
		packetGroup.insert(new InitialPacket(2, 3, entities));
		packetGroup.insert(new JoinPacket());
		packetGroup.insert(new LeavePacket(0));
		packetGroup.insert(new ShootPacket(1, 1, 1, 1));
		packetGroup.insert(new SpawnPacket(1, 1, 1, 1, true));
		
		ByteBuffer buffer = packetParser.getPacketsAsBuffer(packetGroup);
		
		PacketGroup newPacketGroup = packetParser.getPacketsFromBuffer(buffer);
		
		assertEquals(packetGroup, newPacketGroup);
	}
}
