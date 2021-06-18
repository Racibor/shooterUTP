package shooter.server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import shooter.client.ClientNetHandler;
import shooter.game.GameState;
import shooter.server.packet.Packet;
import shooter.server.packet.PacketGroup;
import shooter.server.packet.PacketParser;
import shooter.server.packet.PacketParserConfiguration;
import shooter.server.packet.packets.InitialPacket;
import shooter.server.packet.packets.JoinPacket;

public class ServerTest {
	ClientNetHandler netHandler;
	PacketParser packetParser;
	ExecutorService exec;
	Server server;
	
	public ServerTest() {
		// TODO Auto-generated constructor stub
		packetParser = new PacketParser(PacketParserConfiguration.getDefaultConfiguration());
		exec = Executors.newSingleThreadExecutor();
		try {
			netHandler = new ClientNetHandler(packetParser, 3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@BeforeEach
	public void init() {
		try {
			server = new Server("localhost", 3001, packetParser, new ServerGameController(new GameState(99)));
			exec.execute(server);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
	
	@Test
	public void testJoin() {
		netHandler.setAddress(new InetSocketAddress("localhost", 3001));
		PacketGroup packetGroup = new PacketGroup(-1);
		packetGroup.insert(new JoinPacket());
		
		netHandler.queue(packetGroup);
		netHandler.send();
		
		while(!netHandler.newUpdate()) {
			
		}
		
		PacketGroup response = netHandler.getLastUpdate();
		
		Packet packet = response.getPacketList().get(0);
		
		assertTrue(packet instanceof InitialPacket);
	}
	
	@AfterEach
	public void close() {
		server.shutdown();
	}
}
