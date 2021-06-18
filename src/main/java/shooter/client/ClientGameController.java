package shooter.client;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import shooter.game.GameState;
import shooter.game.entity.Bullet;
import shooter.game.entity.Entity;
import shooter.game.entity.Player;
import shooter.game.entity.Position;
import shooter.server.packet.Packet;
import shooter.server.packet.PacketGroup;
import shooter.server.packet.packets.DeadPacket;
import shooter.server.packet.packets.HitPacket;
import shooter.server.packet.packets.InitialPacket;
import shooter.server.packet.packets.LeavePacket;
import shooter.server.packet.packets.MovementPacket;
import shooter.server.packet.packets.ShootPacket;
import shooter.server.packet.packets.SpawnPacket;

public class ClientGameController {
	GameState gameState;
	Player player;
	public boolean connected = true;
	PacketGroup update;
	int tickRate;
	long tick = -1;

	public ClientGameController(GameState gameState) {
		this.gameState = gameState;
		this.update = new PacketGroup(tick);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getId() {
		return player.getId();
	}

	public void setTickRate(int tickRate) {
		this.tickRate = tickRate;
	}

	public void applyPackets(PacketGroup packetGroup) {
		List<Packet> packets = packetGroup.getPacketList();
		Entity entity;
		for (Packet packet : packets) {
			switch (packet.type) {
			case JOIN:
				break;
			case INITIAL:
				InitialPacket init = (InitialPacket) packet;
				gameState.setEntityList(init.entityList);
				gameState.setSeed(init.seed);
				gameState.setWidth(init.width);
				gameState.setHeight(init.height);
				player = (Player) gameState.getEntityByID(init.id);
				player.setMe(true);
				connected = true;
				break;
			case LEAVE:
				LeavePacket leave = (LeavePacket) packet;
				if (leave.id == player.getId()) {
					connected = false;
				} else {
					gameState.removeEntityById(leave.id);
				}
				break;
			case MOVE:
				MovementPacket move = (MovementPacket) packet;
				if (move.id == player.getId()) {
					Integer id = move.id;
					entity = gameState.getEntityByID(id);
					Position lastPos = entity.getPosition();
					entity.setPosition(new Position(move.x, move.y, move.angle));
				} else {
					Integer id = move.id;
					entity = gameState.getEntityByID(id);
					Position lastPos = entity.getPosition();
					entity.dx = (move.x - entity.localX) / (50 / tickRate);
					entity.dy = (move.y - entity.localY) / (50 / tickRate);
					entity.dAngle = (move.angle - entity.localAngle) / (50 / tickRate);
					entity.setPosition(new Position(move.x, move.y, move.angle));
				}
				break;
			case SPAWN:
				SpawnPacket spawn = (SpawnPacket) packet;
				entity = gameState.getEntityByID(spawn.id);
				Position pos = new Position(spawn.x, spawn.y, spawn.angle);
				if (entity == null) {
					if (spawn.isPlayer) {
						entity = new Player("test", pos);
					} else {
						entity = new Entity("test", pos);
					}
					entity.setId(spawn.id);
					gameState.insertEntity(entity);
					entity.setPosition(pos);
				} else if (entity.getId() == player.getId()) {
					entity.localX = spawn.x;
					entity.localY = spawn.y;
					entity.localAngle = spawn.angle;
				}
				if(entity.isPlayer()) {
					((Player) entity).setHp(Player.BASE_HP);
				}
				entity.setPosition(pos);
				entity.setAlive(true);
				break;
			case SHOOT:
				ShootPacket shoot = (ShootPacket) packet;
				Position position = new Position(shoot.x, shoot.y, shoot.angle);
				Entity bullet = new Bullet(position);
				bullet.setPosition(position);
				bullet.setId(shoot.id);
				float tg = (float) Math.tan(shoot.angle);
				
				bullet.localX = shoot.x;
				bullet.localY = shoot.y;
				bullet.localAngle = shoot.angle;
				float rads = shoot.angle * MathUtils.degreesToRadians;
				bullet.dx = (float) (15 * Math.cos(rads));
				bullet.dy = (float) (15 * Math.sin(rads));
				gameState.insertEntity(bullet);
				break;
			case DEAD:
				DeadPacket dead = (DeadPacket) packet;
				Entity tempEntity = gameState.getEntityByID(dead.id);
				tempEntity.setAlive(false);
				break;
			case HIT:
				HitPacket hit = (HitPacket) packet;
				Player hittedEntity = (Player) gameState.getEntityByID(hit.hittedId);
				hittedEntity.setHp(hit.newHp);
				gameState.removeEntityById(hit.hittingId);
			}
		}
	}

	public void tick() {
		gameState.getEntityList().stream().forEach(e -> {
			float tempX = e.localX + e.dx;
			float tempY = e.localY + e.dy;
			e.localAngle += e.dAngle;
			if (walkableX(tempX)) {
				e.localX = tempX;
			} else {
				if(e instanceof Bullet) {
					gameState.removeEntityById(e.getId());
				}
			}
			if (walkableY(tempY)) {
				e.localY = tempY;
			} else {
				if(e instanceof Bullet) {
					gameState.removeEntityById(e.getId());
				}
			}

		});
	}

	public boolean walkableX(float x) {
		return x > -gameState.getWidth() / 2 && x < gameState.getWidth() / 2;
	}

	public boolean walkableY(float y) {
		return y > -gameState.getHeight() / 2 && y < gameState.getHeight() / 2;
	}

	public PacketGroup getPlayerDelta() {
		update.clear();
		if (player.isAlive()) {
			update.insert(new MovementPacket(player.getId(), player.localX, player.localY, player.localAngle));
		}
		update.setTick(tick + 1);
		return update;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public long getTick() {
		return tick;
	}

	public void setTick(long tick) {
		this.tick = tick;
	}

	public Position shoot() {
		float angle = player.localAngle * MathUtils.degreesToRadians;
		float x = (float) (player.localX + (((player.width)/2)) * Math.cos(angle));
		float y = (float) (player.localY + (((player.height)/2)) * Math.sin(angle));
		return new Position(x, y, player.localAngle);
	}

	public void move(int dir) {
		float tempY;
		float tempX;
		switch (dir) {
		case 0:
			tempY = player.localY - 5;
			if (walkableY(tempY)) {
				player.localY = tempY;
			}
			break;
		case 1:
			tempY = player.localY + 5;
			if (walkableY(tempY)) {
				player.localY = tempY;
			}
			break;
		case 2:
			tempX = player.localX - 5;
			if (walkableX(tempX)) {
				player.localX = tempX;
			}
			break;
		case 3:
			tempX = player.localX + 5;
			if (walkableX(tempX)) {
				player.localX = tempX;
			}
			break;
		}
	}
}
