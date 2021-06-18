package shooter.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import shooter.server.packet.Packet;
import shooter.server.packet.PacketGroup;
import shooter.server.packet.PacketParser;

public class ClientNetHandler {

	private InetSocketAddress address;
	private Selector selector;
	private DatagramChannel clientSocket;
	private ByteBuffer readBuffer = ByteBuffer.allocate(2048);
	private PacketParser packetParser;
	private PacketGroup lastUpdate;
	private PacketGroup myUpdate;
	long lastTick = -2;

	public ClientNetHandler(PacketParser packetParser, int port) throws IOException {
		// TODO Auto-generated constructor stub
		this.packetParser = packetParser;
		selector = Selector.open();
		clientSocket = DatagramChannel.open();
		clientSocket.socket().bind(new InetSocketAddress(port));
		clientSocket.configureBlocking(false);
		clientSocket.register(selector,  SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		lastUpdate = new PacketGroup(lastTick);
		myUpdate = new PacketGroup(lastTick);
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address; 
	}
	

	public PacketGroup getLastUpdate() {
		return lastUpdate;
	}

	public void close() {
		try {
			clientSocket.close();
			selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean newUpdate() {
		int block;
		boolean flag = false;
		PacketGroup packets;
		try {
			block = selector.selectNow();
			if(block > 0) {
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					if(key.isReadable()) {
						lastUpdate.clear();
						packets = read(key);
						if(packets.getTick() > lastUpdate.getTick()) {
							lastTick = packets.getTick();
							lastUpdate = packets;
							flag = true;
						}
						readBuffer.clear();
					}
					iter.remove();
				}
			}
		} catch (IOException e) {
			return false;
		}
		return flag;
	}

	public void queue(Packet packet) {
		myUpdate.insert(packet);
	}
	
	public void queue(PacketGroup packetGroup) {
		myUpdate.insert(packetGroup);
	}

	public void clearUpdates() {
		myUpdate.clear();
	}

	public void send() {
		try {
			myUpdate.setTick(lastTick);
			ByteBuffer buff = packetParser.getPacketsAsBuffer(myUpdate);
			clientSocket.send(buff, address);
			myUpdate.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PacketGroup read(SelectionKey key) throws IOException {
		readBuffer.clear();
		DatagramChannel channel = (DatagramChannel)key.channel();
		channel.receive(readBuffer);
		readBuffer.flip();
		PacketGroup packetGroup = packetParser.getPacketsFromBuffer(readBuffer);
		return packetGroup;
	}
	
}
