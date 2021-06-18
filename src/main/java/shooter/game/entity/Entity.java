package shooter.game.entity;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.Sprite;

import shooter.util.CyclicBuffer;

public class Entity {
	
	private final static int POS_SIZE = 8;
	
	int id;
	
	
	private String name;
	
	private boolean isAlive;
	
	private boolean isPlayer;
	
	public float localX;
	
	public float localY;
	
	public float width;
	
	public float height;
	
	public float localAngle;
	
	public float dx;
	
	public float dy;
	
	public float dAngle;
	
	public long lastTick;
	
	private Position lastPosition;
	
	private CyclicBuffer<Position> position;
	
	
	
	public Entity(String name, Position position) {
		this.name = name;
		this.position = new CyclicBuffer<>(Entity.POS_SIZE);
		this.setPosition(position);
		this.isAlive = false;
		this.isPlayer = false;
	}
	
	public Entity(String name, Position position, boolean isPlayer) {
		this.name = name;
		this.isAlive = false;
		this.position = new CyclicBuffer<>(Entity.POS_SIZE);
		this.setPosition(position);
		this.isPlayer = isPlayer;
	}
	
	
	public Position getPosition() {
		return lastPosition;
	}
	
	public Position getPosition(int index) {
		return position.get(index);
	}
	
	public void setPosition(Position position) {
		this.position.insert(position);
		lastPosition = position;
	}
	
	public boolean isPlayer() {
		return isPlayer;
	}
	
	public void setAlive(boolean alive) {
		isAlive = alive;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Entity entity) {
		return name.equals(entity.getName()) && entity.isPlayer() == isPlayer && entity.getId() == id;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (Float.floatToIntBits(dAngle) != Float.floatToIntBits(other.dAngle))
			return false;
		if (id != other.id)
			return false;
		if (isPlayer != other.isPlayer())
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
}
