package shooter.game.entity;


public class Position {
	
	private float x;
	
	private float y;
	
	private float angle;
	
	
	public Position(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	
	
	public float getAngle() {
		return angle;
	}


	
	public void setAngle(float angle) {
		this.angle = angle;
	}


	
	public float getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}


	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (Float.floatToIntBits(angle) != Float.floatToIntBits(other.angle))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	
	
	
}
