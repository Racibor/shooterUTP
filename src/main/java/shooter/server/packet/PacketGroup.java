package shooter.server.packet;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class PacketGroup {
	
	long tick;
	
	
	private InetSocketAddress address;
	
	private List<Packet> packetList;
	
	
	public PacketGroup(long tick) {
		this.tick = tick;
		packetList = new ArrayList<>();
	}
	
	
	public void insert(PacketGroup packetGroup) {
		packetList.addAll(packetGroup.getPacketList());
	}
	
	public void insert(Packet packet) {
		packetList.add(packet);
	}
	
	public List<Packet> getPacketList() {
		return packetList;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}
	
	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}
	
	public long getTick() {
		return tick;
	}
	
	public void setTick(long tick) {
		this.tick = tick;
	}
	
	public void clear() {
		packetList.clear();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PacketGroup other = (PacketGroup) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (packetList == null) {
			if (other.packetList != null)
				return false;
		} else if (!packetList.equals(other.packetList))
			return false;
		if (tick != other.tick)
			return false;
		return true;
	}
	
	
}
