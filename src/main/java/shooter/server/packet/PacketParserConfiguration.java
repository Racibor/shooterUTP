package shooter.server.packet;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import shooter.Main;
import shooter.reflection_utils.ClassUtils;
import shooter.server.packet.packets.*;

public class PacketParserConfiguration {
	
	Map<Integer, Parser<Packet, ByteBuffer>> handlers;
	
	Map<Integer, Parser<ByteBuffer, Packet>> parsers;
	
	public PacketParserConfiguration() {
		// TODO Auto-generated constructor stub
		handlers = new TreeMap<>();
		parsers = new TreeMap<>();
	}
	
	
	public PacketParserConfiguration registerHandler(ServerPacketType type, Parser<Packet, ByteBuffer> handler) {
		handlers.put(type.code, handler);
		return this;
	}
	
	public PacketParserConfiguration registerParser(ServerPacketType type, Parser<ByteBuffer, Packet> parser) {
		parsers.put(type.code, parser);
		return this;
	}
	
	public static PacketParserConfiguration getDefaultConfiguration() {
		PacketParserConfiguration config = new PacketParserConfiguration();
		String temp = PacketService.class.getPackageName();
		List<Class<?>> classes = ClassUtils.getClasses(PacketService.class.getPackageName());
		classes = classes.stream()
				.filter(clazz -> {
					Class<?> superclass = clazz.getSuperclass();
					if(superclass != null) {
						if(superclass.getSimpleName().contains("Packet")) {
							return true;
						}
					}
					return false;
				}).collect(Collectors.toList());

		classes = classes.stream().filter(clazz -> {
					Annotation[] annotations = clazz.getAnnotations();
					return clazz.isAnnotationPresent(DataPacket.class);
				}).collect(Collectors.toList());

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		for(Class<?> clazz : classes) {
			try {
				Class<?> tempService = Class.forName(clazz.getName() + "Service", true, classLoader);
				PacketService service = (PacketService) tempService.getDeclaredConstructor().newInstance();

				Annotation[] annotations = clazz.getAnnotationsByType(DataPacket.class);
				DataPacket dataPacket = (DataPacket) annotations[0];


				config
						.registerHandler(dataPacket.type(), service::fromByteBuffer)
						.registerParser(dataPacket.type(), service::toByteBuffer);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Some Service classes are missing");
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}


//		MovementPacketService movementPacketService = new MovementPacketService();
//		ShootPacketService shootPacketService = new ShootPacketService();
//		JoinPacketService joinPacketService = new JoinPacketService();
//		LeavePacketService leavePacketService = new LeavePacketService();
//		InitPacketService initPacketService = new InitPacketService();
//		SpawnPacketService spawnPacketService = new SpawnPacketService();
//		HitPacketService hitPacketService = new HitPacketService();
//		DeadPacketService deadPacketService = new DeadPacketService();
//
//
//		config
//		.registerHandler(ServerPacketType.MOVE, movementPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.MOVE, movementPacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.SHOOT, shootPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.SHOOT, shootPacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.JOIN, joinPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.JOIN, joinPacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.LEAVE, leavePacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.LEAVE, leavePacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.INITIAL, initPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.INITIAL, initPacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.SPAWN, spawnPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.SPAWN, spawnPacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.HIT, hitPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.HIT, hitPacketService::toByteBuffer)
//
//		.registerHandler(ServerPacketType.DEAD, deadPacketService::fromByteBuffer)
//		.registerParser(ServerPacketType.DEAD, deadPacketService::toByteBuffer);
		return config;
	}


}
