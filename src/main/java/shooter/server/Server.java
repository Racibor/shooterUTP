package shooter.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import shooter.server.packet.PacketGroup;
import shooter.server.packet.PacketParser;
import shooter.server.packet.ServerPacketHandler;


public class Server implements Runnable {
	
	private Selector selector;
	
	private DatagramChannel serverSocket;
	
	private ByteBuffer readBuffer = ByteBuffer.allocate(2048);
	
	private PacketParser packetParser;
	
	private ServerPacketHandler packetHandler;
	
	int tickRate = 24;
	
	volatile boolean running;

	
	public Server(String ip, int port, PacketParser packetParser, ServerGameController gameController)
			throws BindException, IOException {
			selector = Selector.open();
			// serverSocket = ServerSocketChannel.open();
			serverSocket = DatagramChannel.open();

			serverSocket.socket().bind(new InetSocketAddress(ip, port));
			serverSocket.configureBlocking(false);
			serverSocket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			this.packetHandler = new ServerPacketHandler(gameController);
			this.packetParser = packetParser;
			running = true;
	}

	long time = System.nanoTime();

	
	public void run() {
		while (running) {
			try {
				int block = selector.selectNow();
				if (block > 0) {
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> iter = selectedKeys.iterator();
					while (iter.hasNext()) {
						SelectionKey key = iter.next();
						if (key.isReadable()) {
							PacketGroup packets = read(key);
							packetHandler.handlePackets(packets);
							readBuffer.clear();
						}
						iter.remove();
					}
				}
				long cur = System.nanoTime();
				if (cur - time > 1000000000 / tickRate) {
					Map<InetSocketAddress, PacketGroup> responses = packetHandler.getResponse();
					responses.forEach((addr, packets) -> {
						if(packets != null) {
							ByteBuffer buff = packetParser.getPacketsAsBuffer(packets);
							try {
								serverSocket.send(buff, addr);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					packetHandler.tick();
					time = cur;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			selector.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void shutdown() {
		Map<InetSocketAddress, PacketGroup> responses = packetHandler.kickAll();
		responses.forEach((addr, packets) -> {
			ByteBuffer buff = packetParser.getPacketsAsBuffer(packets);
			try {
				serverSocket.send(buff, addr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		running = false;

	}

	
	private PacketGroup read(SelectionKey key) throws IOException {
		readBuffer.clear();
		DatagramChannel channel = (DatagramChannel) key.channel();

		InetSocketAddress addr = (InetSocketAddress) channel.receive(readBuffer);
		readBuffer.flip();
		PacketGroup packetGroup = packetParser.getPacketsFromBuffer(readBuffer);

		packetGroup.setAddress(addr);
		return packetGroup;
	}

}
