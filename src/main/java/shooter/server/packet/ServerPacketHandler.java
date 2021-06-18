package shooter.server.packet;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import shooter.game.entity.Player;
import shooter.server.ServerGameController;
import shooter.server.packet.packets.LeavePacket;

public class ServerPacketHandler {
	
	private ServerGameController gameController;
	
	private Map<InetSocketAddress, Player> playerMap = new HashMap<>();
	
	public ServerPacketHandler(ServerGameController gameController) {
		this.gameController = gameController;
	}
	
	public void handlePackets(PacketGroup packetGroup) {
		InetSocketAddress addr = packetGroup.getAddress();
		if(!playerMap.containsKey(addr)) {
			Player player = gameController.registerPlayer();
			playerMap.put(addr, player);
		}
		gameController.applyPackets(playerMap.get(addr), packetGroup);
	}
	
	
	public Map<InetSocketAddress, PacketGroup> getResponse() {
		Map<InetSocketAddress, PacketGroup> responses = new HashMap<>();
		playerMap.forEach((key, val) -> {
			PacketGroup response = gameController.getDeltaForPlayer(val);
			responses.put(key, response);
		});
		return responses;
	}
	
	public Map<InetSocketAddress, PacketGroup> kickAll() {
		Map<InetSocketAddress, PacketGroup> responses = new HashMap<>();
		playerMap.forEach((key, val) -> {
			PacketGroup response = new PacketGroup(Long.MAX_VALUE);
			response.insert(new LeavePacket(val.getId()));
			responses.put(key, response);
		});
		return responses;
	} 
	
	public void tick() {
		gameController.tick();
	}
}
