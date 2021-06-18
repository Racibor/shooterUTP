package shooter.server.packet;


public abstract class Packet {
	
	public int size;
	
	public ServerPacketType type;
	
	public Packet() {
		size = 0;
	}
	
	public abstract int getSize();
	
	public abstract boolean equals(Object obj);
}
