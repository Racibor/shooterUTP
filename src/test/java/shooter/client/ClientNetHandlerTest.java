package shooter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shooter.server.packet.PacketGroup;
import shooter.server.packet.PacketParser;
import shooter.server.packet.PacketParserConfiguration;
import shooter.server.packet.packets.JoinPacket;

public class ClientNetHandlerTest {
	ClientNetHandler netHandler;
	DatagramSocket datagramSocket;
	PacketParser packetParser;

	public ClientNetHandlerTest() {
		packetParser = new PacketParser(PacketParserConfiguration.getDefaultConfiguration());
		try {
			netHandler = new ClientNetHandler(packetParser, 2000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@BeforeEach
	public void init() {
		try {
			datagramSocket = new DatagramSocket(2001);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNetHandler() {

		try {
			netHandler.setAddress(new InetSocketAddress("localhost", 2001));
			assertTrue(!netHandler.newUpdate());

			PacketGroup packetGroup = new PacketGroup(-1);
			packetGroup.insert(new JoinPacket());

			netHandler.queue(packetGroup);
			netHandler.send();

			byte[] buff = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			datagramSocket.receive(packet);

			PacketGroup newPacketGroup = packetParser.getPacketsFromBuffer(ByteBuffer.wrap(buff));
			newPacketGroup.setTick(-1);
			assertEquals(newPacketGroup, packetGroup);
			
			DatagramPacket newPacket = new DatagramPacket(packetParser.getPacketsAsBuffer(newPacketGroup).array(), buff.length, new InetSocketAddress("localhost", 2000));
			
			datagramSocket.send(newPacket);
			
			assertTrue(netHandler.newUpdate());
			
			PacketGroup packets = netHandler.getLastUpdate();
			
			assertEquals(packetGroup, packets);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
