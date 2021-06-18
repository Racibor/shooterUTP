package shooter.game.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import shooter.game.GameState;
import shooter.game.entity.Bullet;
import shooter.game.entity.Entity;
import shooter.game.entity.Player;
import shooter.game.entity.Position;
import shooter.util.PixmapGenerator;

public class GameStateRenderer {
	private TextureRegion playerTexture;
	private TextureRegion worldTexture;
	private TextureRegion bulletTexture;
	private TextureRegion HpBar;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	
	public GameStateRenderer(SpriteBatch batch, OrthographicCamera camera, AssetManager assetManager) {
		this.batch = batch;
		this.camera = camera;
		this.assetManager = assetManager;
		playerTexture = new TextureRegion(assetManager.get("assets/skins/defaultSkin.png", Texture.class));
		worldTexture = new TextureRegion(new Texture(PixmapGenerator.getTexture(1000, 500, 0, 0.5f, 0, 1)));
		bulletTexture = new TextureRegion(assetManager.get("assets/skins/bulletSkin.png", Texture.class));
		HpBar = new TextureRegion(new Texture(PixmapGenerator.getTexture(20, 2, 1, 0, 0, 0.8f)));
	}

	
	public void renderState(GameState gameState) {
		batch.draw(worldTexture, -gameState.getWidth()/2, -gameState.getHeight()/2);
		gameState.getEntityList().forEach(this::renderEntity);
	}
	
	private void renderEntity(Entity entity) {
		if(entity.isAlive()) {
			if(entity.isPlayer()) {
				renderPlayer((Player)entity);
				return;
			}
			
			Position pos = entity.getPosition();
			batch.draw(playerTexture, entity.localX - playerTexture.getRegionWidth()/2, entity.localY - playerTexture.getRegionHeight()/2, playerTexture.getRegionWidth()/2, playerTexture.getRegionHeight()/2, playerTexture.getRegionWidth(), playerTexture.getRegionHeight(), 1, 1, entity.localAngle);
		} else {
			if(entity instanceof Bullet) {
				batch.draw(bulletTexture, entity.localX - bulletTexture.getRegionWidth()/2, entity.localY - bulletTexture.getRegionHeight()/2, bulletTexture.getRegionWidth()/2, bulletTexture.getRegionHeight()/2, bulletTexture.getRegionWidth(), bulletTexture.getRegionHeight(), 1, 1, entity.localAngle);
				return;
			}
		}
	}
	
	private void renderPlayer(Player player) {
		Position pos = player.getPosition();
		if(player.isMe()) {
			batch.draw(playerTexture, player.localX - playerTexture.getRegionWidth()/2, player.localY - playerTexture.getRegionHeight()/2, playerTexture.getRegionWidth()/2, playerTexture.getRegionHeight()/2, playerTexture.getRegionWidth(), playerTexture.getRegionHeight(), 1, 1, player.localAngle);
		} else {
			batch.draw(playerTexture, player.localX - playerTexture.getRegionWidth()/2, player.localY - playerTexture.getRegionHeight()/2, playerTexture.getRegionWidth()/2, playerTexture.getRegionHeight()/2, playerTexture.getRegionWidth(), playerTexture.getRegionHeight(), 1, 1, player.localAngle);
		}
		batch.draw(HpBar, player.localX - HpBar.getRegionWidth()/2, player.localY + player.getRadius(), 0, 0, 2*(player.getHp()/10), HpBar.getRegionHeight(), 1, 1, 0);
	}
}
