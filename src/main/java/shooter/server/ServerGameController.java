package shooter.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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


public class ServerGameController {
	
	Random random;
	
	GameState gameState;
	
	PacketGroup globalDelta;
	
	Map<Player, PacketGroup> localPlayerDeltas;
	
	long tick = -1;
	
	int id = 0;
	
	
	public ServerGameController(GameState gameState) {
		// TODO Auto-generated constructor stub
		random = new Random(new Date().getTime());
		this.localPlayerDeltas = new HashMap<>();
		globalDelta = new PacketGroup(tick);
		this.gameState = gameState;
	}
	
	
	public Player registerPlayer() {
		Position pos = new Position((float)(random.nextInt()%500), (float)(random.nextInt()%500), 0);
		Player player = new Player("temp", pos);
		player.setId(nextId());
		player.setAlive(false);
		gameState.insertEntity(player);
		localPlayerDeltas.put(player, new PacketGroup(tick));
		return player;
	}
	
	
	public void applyPackets(Player player, PacketGroup packetGroup) {
		List<Packet> packets = packetGroup.getPacketList();
		Player tempPlayer = (Player) gameState.getEntityByID(player.getId());
		tempPlayer.lastTick = packetGroup.getTick();
		for(Packet packet : packets) {
			switch(packet.type) {
				case JOIN:
					if(tempPlayer != null) {
						InitialPacket init = new InitialPacket();
						init.seed = gameState.getSeed();
						init.entityList = gameState.getEntityList().stream().filter(e -> !(e instanceof Bullet)).collect(Collectors.toList());
						init.id = player.getId();
						localPlayerDeltas.get(player).insert(init);
					}
					break;
				case INITIAL:
					
					break;
				case LEAVE:
					gameState.removeEntityById(tempPlayer.getId());
					localPlayerDeltas.remove(tempPlayer);
					LeavePacket leave = (LeavePacket) packet;
					leave.id = tempPlayer.getId();
					globalDelta.insert(leave);
					break;
				case MOVE:
					MovementPacket move = (MovementPacket) packet;
					if(tempPlayer != null) {
						if(tempPlayer.isAlive()) {
							if(walkableX(move.x) && walkableY(move.y)) {
								Position pos = new Position(move.x, move.y, move.angle);
								tempPlayer.setPosition(pos);
								move.setId(player.getId());
								globalDelta.insert(move);
							}
						}
					}
					break;
				case SPAWN:
					SpawnPacket spawn = (SpawnPacket) packet;
					if(tempPlayer != null) {
						if(!tempPlayer.isAlive()) {
							Position pos = new Position((float)(-gameState.getWidth()/2 + Math.abs(random.nextInt())%(gameState.getWidth()-10)), (float)-gameState.getHeight()/2 + (Math.abs(random.nextInt())%(gameState.getHeight()-10)), 0);
							tempPlayer.setPosition(pos);
							tempPlayer.setAlive(true);
							tempPlayer.setHp(Player.BASE_HP);
							spawn.setId(player.getId());
							spawn.x = pos.getX();
							spawn.y = pos.getY();
							spawn.angle = pos.getAngle();
							globalDelta.insert(spawn);
						}
					}
					break;
				case SHOOT:
					ShootPacket shoot = (ShootPacket) packet;
					if(tempPlayer.isAlive()) {
						Position position = new Position(shoot.x, shoot.y, shoot.angle);
						
						Entity bullet = new Bullet(position);
						((Bullet) bullet).owner = player.getId();
						bullet.setId(nextId());
						float rads = MathUtils.degreesToRadians * shoot.angle;
						bullet.dx = (float) (15*Math.cos(rads));
						bullet.dy = (float) (15*Math.sin(rads));
						gameState.insertEntity(bullet);
						shoot.id = bullet.getId();
						globalDelta.insert(shoot);
					}
					break;
			}
		}
	} 
	
	public boolean walkableX(float x) {
		return x > -gameState.getWidth() / 2 && x < gameState.getWidth() / 2;
	}
	
	public boolean walkableY(float y) {
		return y > -gameState.getHeight() / 2 && y < gameState.getHeight() / 2;
	}
	
	public PacketGroup getDeltaForPlayer(Player player) {
		PacketGroup global = globalDelta;
		PacketGroup delta = localPlayerDeltas.get(player);
		if(delta != null) {
			delta.insert(globalDelta);
			delta.setTick(tick);
		}
		return delta;
	}

	
	public void tick() {
		tick++;
		globalDelta.clear();
		localPlayerDeltas.forEach((key, val) -> {
			val.clear();
		});
		gameState.getEntityList().stream().filter(e -> (e instanceof Bullet)).map(e -> (Bullet) e).forEach(e -> {
			e.lastTick++;
			Position pos = e.getPosition();
			float tx = pos.getX() + e.dx;
			float ty = pos.getY() + e.dy;
			if(walkableX(tx) && walkableY(ty)) {
				Position newPos = new Position(pos.getX() + e.dx, pos.getY() + e.dy, pos.getAngle());
				e.setPosition(newPos);
			} else {
				gameState.removeEntityById(e.getId());
			}
			gameState.getEntityList().stream().filter(player -> !(player instanceof Bullet) && player.isAlive()).forEach(player -> {
				if(e.collidesWith(player)) {
					int newHp = ((Player)player).getHp() - e.damage;
					if(newHp > 0) {
						((Player)player).setHp(newHp);
						globalDelta.insert(new HitPacket(e.getId(), player.getId(), newHp));
						gameState.removeEntityById(e.getId());
					} else {
						player.setAlive(false);
						globalDelta.insert(new DeadPacket(player.getId()));
					}
					
				}
			});
		});
		
	}
	
	private int nextId() {
		int tmp = id;
		id++;
		return tmp;
	}

}
