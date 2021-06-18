package shooter.client;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.MathUtils;

import shooter.client.ClientGameController;
import shooter.game.GameState;
import shooter.game.entity.Bullet;
import shooter.game.entity.Entity;
import shooter.game.entity.Player;
import shooter.game.entity.Position;
import shooter.server.packet.PacketGroup;
import shooter.server.packet.packets.DeadPacket;
import shooter.server.packet.packets.HitPacket;
import shooter.server.packet.packets.InitialPacket;
import shooter.server.packet.packets.MovementPacket;
import shooter.server.packet.packets.ShootPacket;
import shooter.server.packet.packets.SpawnPacket;
public class ClientGameControllerTest {
	int baseSeed = 9999;
	GameState gameState;
	ClientGameController client;
	Entity entity;

	@BeforeEach
	public void init() {
		gameState = new GameState(baseSeed);
		client = new ClientGameController(gameState);
		PacketGroup packetGroup = new PacketGroup(-1);
		InitialPacket init = new InitialPacket();
		entity = new Player("test", new Position(1, 1, 1));
		entity.setId(0);
		init.entityList.add(entity);
		init.id = entity.getId();
		packetGroup.insert(init);

		client.applyPackets(packetGroup);
	}

	@Test
	public void testInitialPacket() {
		Entity newEntity = client.getGameState().getEntityList().get(0);

		assertEquals(newEntity, entity);
		assertEquals(entity, client.getPlayer());
	}

	@Test
	public void testMovePacket() {
		PacketGroup packetGroup = new PacketGroup(-1);
		Position oldPosition = entity.getPosition();
		MovementPacket move = new MovementPacket(entity.getId(), 2, 2, 2);
		packetGroup.insert(move);
		client.applyPackets(packetGroup);

		Position newPosition = gameState.getEntityList().get(0).getPosition();
		assertEquals(newPosition.getX(), move.x);
		assertEquals(newPosition.getY(), move.y);
		assertEquals(newPosition.getAngle(), move.angle);
		Position previousPosition = gameState.getEntityList().get(0).getPosition(-2);
		assertEquals(previousPosition, oldPosition);
	}

	@Test
	public void testSpawnPacket() {
		assertTrue(!entity.isAlive());

		PacketGroup packetGroup = new PacketGroup(-1);
		SpawnPacket spawn = new SpawnPacket(entity.getId(), 0, 0, 0, true);

		packetGroup.insert(spawn);
		client.applyPackets(packetGroup);

		assertTrue(entity.isAlive());
	}

	@Test
	public void testShootPacket() {
		Position pos = client.shoot();
		PacketGroup packetGroup = new PacketGroup(-1);
		ShootPacket shoot = new ShootPacket(entity.getId(), pos.getX(), pos.getY(), pos.getAngle());

		packetGroup.insert(shoot);
		client.applyPackets(packetGroup);

		Entity tentity = client.getGameState().getEntityList().get(0);
		assertTrue(tentity instanceof Bullet);
		float rads = MathUtils.degreesToRadians * shoot.angle;
		float dx = (float) (15 * Math.cos(rads));
		float dy = (float) (15 * Math.sin(rads));
		assertEquals(dx, tentity.dx);
		assertEquals(dy, tentity.dy);
	}

	@Test
	public void testDeadPacket() {
		entity.setAlive(true);
		PacketGroup packetGroup = new PacketGroup(-1);
		DeadPacket dead = new DeadPacket(entity.getId());

		packetGroup.insert(dead);
		client.applyPackets(packetGroup);

		assertTrue(!entity.isAlive());
	}

	@Test
	public void testHitPacket() {
		entity.setAlive(true);
		Position pos = client.shoot();
		PacketGroup packetGroup = new PacketGroup(-1);
		ShootPacket shoot = new ShootPacket(1, pos.getX(), pos.getY(), pos.getAngle());
		packetGroup.insert(shoot);
		client.applyPackets(packetGroup);

		Bullet bullet = (Bullet) client.getGameState().getEntityByID(1);

		packetGroup = new PacketGroup(0);
		HitPacket dead = new HitPacket(bullet.getId(), client.getPlayer().getId(), 70);

		packetGroup.insert(dead);
		client.applyPackets(packetGroup);

		assertEquals(client.getPlayer().getHp(), dead.newHp);
		assertNull(client.getGameState().getEntityByID(bullet.getId()));
	}
	
	@Test
	public void testTick() {
		PacketGroup packetGroup = new PacketGroup(-1);
		MovementPacket move = new MovementPacket(entity.getId(), 2, 2, 2);
		packetGroup.insert(move);
		client.applyPackets(packetGroup);
		
		Player player = client.getPlayer();
		float tempLocalX = player.localX;
		float tempLocalY = player.localY;
		float tempLocalAngle = player.localAngle;
		
		client.tick();
		assertEquals(tempLocalX + player.dx, player.localX);
		assertEquals(tempLocalY + player.dy, player.localY);
		assertEquals(tempLocalAngle + player.dAngle, player.localAngle);
	}
	
	@Test
	public void testWalkables() {
		gameState.setWidth(200);
		gameState.setHeight(200);
		assertTrue(client.walkableX(99));
		assertTrue(!client.walkableX(101));
		assertTrue(client.walkableX(-99));
		assertTrue(!client.walkableX(-101));
		
		assertTrue(client.walkableY(99));
		assertTrue(!client.walkableY(101));
		assertTrue(client.walkableY(-99));
		assertTrue(!client.walkableY(-101));
	}
	
	@Test
	public void testMove() {
		
	}
}
