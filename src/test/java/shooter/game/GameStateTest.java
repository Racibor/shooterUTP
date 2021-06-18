package shooter.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import shooter.game.entity.Entity;
import shooter.game.entity.Position;

public class GameStateTest {
	int seed = 9999;
	GameState gameState = new GameState(seed);
	
	
	@Test
	void entityListIsInitialized() {
		assertNotNull(gameState.getEntityList());
	}
	
	@Test
	void getEntityListTest() {
		assertTrue(gameState.getEntityList() instanceof List);
	}
	
	@Test
	void insertEntityTest() {
		Entity entity = new Entity("test", new Position(1, 1, 1));
		gameState.insertEntity(entity);
		
		Entity entity2 = gameState.getEntityList().get(0);
		assertTrue(entity == entity2);
	}
	@Test
	void setEntityListTest() {
		gameState.setEntityList(new ArrayList<>());
		Entity entity = new Entity("test", new Position(1, 1, 1));
		gameState.insertEntity(entity);
		int sizeBefore = gameState.getEntityList().size();
		Entity entity2 = new Entity("test2", new Position(1, 1, 1));
		List<Entity> entityList = Arrays.asList(entity2);
		gameState.setEntityList(entityList);
		assertEquals(entityList.size(), gameState.getEntityList().size());
	}
	
	
}
