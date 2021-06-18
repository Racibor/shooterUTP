package shooter.game.entity;

import com.badlogic.gdx.math.MathUtils;

public class Bullet extends Entity {

	public float width = 10;
	public float height = 2;
	public int damage = 20;
	public int owner;

	public Bullet(Position position) {
		super("bullet", position);
		this.width = 2;
		this.height = 10;
	}

	public boolean collidesWith(Entity entity) {
		boolean flag = false;
		if(entity.getId() == owner) {
			return false;
		}
		if (entity instanceof Player) {
			int delta = (int) (entity.lastTick - this.lastTick);
			float ang = MathUtils.degreesToRadians * entity.localAngle;
			float sin = (float) Math.sin(ang);
			float cos = (float) Math.cos(ang);
			Position entityPos = entity.getPosition(-delta);
			Position bulletPos = this.getPosition();
			float cp = (bulletPos.getX() - entityPos.getX()) * (bulletPos.getX() -  entityPos.getX())
					+ (bulletPos.getY() -  entityPos.getY()) * (bulletPos.getY() -  entityPos.getY());
			float sqrCheck = ((Player) entity).getRadius();
			sqrCheck = sqrCheck * sqrCheck;
			float tempX = (entityPos.getX() + ((entity.width) / 2) * cos);
			float tempY = (entityPos.getY() + ((entity.height) / 2) * sin);
			float cp2 = (bulletPos.getX() - tempX) * (bulletPos.getX() - tempX) + (bulletPos.getY() - tempY) * (bulletPos.getY() - tempY);
			if (cp < sqrCheck || cp2 < sqrCheck) {
				flag = true;
			}
		}
		return flag;
	}
}
