package shooter.game.entity;

import shooter.util.CyclicBuffer;

public class Player extends Entity {
	
	public static int BASE_HP = 100;
	
	boolean isMe;
	
	private float radius = 10;
	
	int hp = BASE_HP;
	
	
	public boolean isMe() {
		return isMe;
	}
	
	
	public float getRadius() {
		return radius;
	}

	
	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}
	
	public int getHp() {
		return hp;
	}

	
	public void setHp(int hp) {
		this.hp = hp;
	}


	public Player(String name, Position position) {
		super(name, position, true);
		this.width = 30;
		this.height = 30;
	}
}
