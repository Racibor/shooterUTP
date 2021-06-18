package shooter.server.packet;

import java.util.HashMap;
import java.util.Map;


public enum ServerPacketType {
	
	JOIN(0),
	
	LEAVE(2), SPAWN(3), MOVE(4), INITIAL(5), SHOOT(6), HIT(7), DEAD(8);
	
	private static Map<Integer, ServerPacketType> mapping = new HashMap<>();
	
	public Integer code;

	static {
		for (ServerPacketType type : ServerPacketType.values()) {
			mapping.put(type.code, type);
		}
	}
	
	private ServerPacketType(Integer val) {
		code = val;
	}
	
	public static ServerPacketType valueOf(Integer type) {
		return mapping.get(type);
	}
}
