package shooter.game;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import shooter.game.entity.Entity;
import shooter.game.entity.Player;

public class GameState {

	int seed;
	private Map<Integer, Entity> entityList;
	int width = 1000;
	int height = 500;
	Random random;
	
	public GameState(int seed) {
		this.entityList = new HashMap<>();
		this.seed = seed;
		random = new Random(seed);
	}
	
	public void insertEntity(Entity entity) {
		entityList.put(entity.getId(), entity);
	}
	
	public Entity getEntityByID(int id) {
		return entityList.get(id);
	}

	public void removeEntityById(Integer id) {
		entityList.remove(id);
	}

	public List<Entity> getEntityList() {
		return (List<Entity>) new ArrayList<Entity>(entityList.values());
	}

	public void setEntityList(List<Entity> entityList) {
		this.entityList.clear();
		entityList.forEach(e -> this.entityList.put(e.getId(), e));
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
	
}
