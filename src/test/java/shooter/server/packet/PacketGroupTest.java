package shooter.server.packet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import shooter.game.entity.Entity;
import shooter.game.entity.Position;
import shooter.server.packet.packets.MovementPacket;

public class PacketGroupTest {
	PacketGroup packetGroup;
	
	@Test
	public void testInsert() {
		PacketGroup tempPacketGroup = new PacketGroup(-1);
		
		MovementPacket mov1 = new MovementPacket();
		MovementPacket mov2 = new MovementPacket();
		tempPacketGroup.insert(mov1);
		tempPacketGroup.insert(mov2);
		
		MovementPacket mov3 = new MovementPacket();
		
		
		packetGroup = new PacketGroup(-1);
		packetGroup.insert(mov3);
		packetGroup.insert(tempPacketGroup);
		assertEquals(packetGroup.getPacketList().size(), 3);
		packetGroup.clear();
		assertEquals(packetGroup.getPacketList().size(), 0);
	}
}
