package shooter.game.scenes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import shooter.client.ClientGameController;
import shooter.client.ClientNetHandler;
import shooter.game.GameState;
import shooter.game.entity.Player;
import shooter.game.entity.Position;
import shooter.game.render.GameStateRenderer;
import shooter.server.Server;
import shooter.server.ServerGameController;
import shooter.server.packet.PacketGroup;
import shooter.server.packet.packets.JoinPacket;
import shooter.server.packet.packets.LeavePacket;
import shooter.server.packet.packets.ShootPacket;
import shooter.server.packet.packets.SpawnPacket;
import shooter.util.PixmapGenerator;


public class GameScene extends ScreenAdapter {
	
	GameAdapter game;
	
	ClientNetHandler netHandler;
	
	ClientGameController client;
	
	GameStateRenderer renderer;
	
	OrthographicCamera camera;
	
	SpriteBatch batch;
	
	Server server;
	
	int tickRate;
	
	private long time;
	
	Stage menu;
	Skin skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
	
	Texture menuSprite = new Texture(PixmapGenerator.getTexture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 1, 1, 1, 0.5f));
	
	final TextButton spawn = new TextButton("Spawn", skin);
	
	final TextButton leave = new TextButton("Leave", skin);
	
	boolean renderMenu = true;
	
	int menuTimeout = 0;
	
	int shootTimeout = 0;
	
	public GameScene(GameAdapter game, ClientNetHandler netHandler) {
		this(game, netHandler, null);
	}
	
	public GameScene(GameAdapter game, ClientNetHandler netHandler, Server server) {
		this.server = server;
		this.game = game;
		this.client = game.client;
		this.netHandler = netHandler;
		this.tickRate = game.tickRate;
		initListeners();
	}
	
	public void initListeners() {
		spawn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!client.getPlayer().isAlive()) {
					netHandler.queue(new SpawnPacket(client.getPlayer().getId(), 0.0f, 0.0f, 0.0f, true));
				}
				hideMenu();
			}
		});
		
		leave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				netHandler.queue(new LeavePacket());
				netHandler.send();
				netHandler.close();
				game.setScreen(new MainScene(game, "disconnected"));
			}
		});
	}

	public void dispose() {
		
	}
	
	public void showMenu() {
		renderMenu = true;
		leave.setDisabled(false);
		spawn.setDisabled(false);
		leave.setTouchable(Touchable.enabled);
		Gdx.input.setInputProcessor(menu);
	}
	
	public void hideMenu() {
		renderMenu = false;
		leave.setDisabled(true);
		spawn.setDisabled(true);
		leave.setTouchable(Touchable.disabled);
		Gdx.input.setInputProcessor(null);
	}
	
	public void hide() {
		if(server != null) {
			this.server.shutdown();
		}
	}
	
	public void render(float delta) {
		if (netHandler.newUpdate()) {
			PacketGroup update = netHandler.getLastUpdate();
			if (update != null) {
				client.applyPackets(update);
			}
			PacketGroup myUpdate = client.getPlayerDelta();
			netHandler.queue(myUpdate);
			netHandler.send();
		}
		Player player = client.getPlayer();
		if(!client.connected) {
			game.setScreen(new MainScene(game, "Disconnected"));
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			client.move(0);
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			client.move(1);
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			client.move(2);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			client.move(3);
		}
		if(shootTimeout > 0) {
			shootTimeout--;
		}
		if(Gdx.input.isTouched()) {
			if(shootTimeout == 0) {
				Position pos = client.shoot();
				ShootPacket shoot = new ShootPacket();
				shoot.id = player.getId();
				shoot.x = pos.getX();
				shoot.y = pos.getY();
				shoot.angle = pos.getAngle();
				netHandler.queue(shoot);
				shootTimeout = 5;
			}
		}
		if(menuTimeout > 0) {
			menuTimeout--;
		}
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			if(menuTimeout == 0) {
				if(renderMenu) {
					hideMenu();
				} else {
					showMenu();
				}
				menuTimeout = 20;
			}
		}
		
		float xInput = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;// camera.viewportWidth/2;//;
		float yInput = -(Gdx.input.getY() - Gdx.graphics.getHeight() / 2);// camera.viewportHeight/2;//;
		float angle = MathUtils.radiansToDegrees * MathUtils.atan2(yInput, xInput);
		if (angle < 0) {
			angle += 360;
		}
		player.localAngle = angle;
		long cur = System.nanoTime();

		if (cur - time > (1000000000 / tickRate) / 2) {
			client.tick();
			time = cur;
		}
		// camera.translate(player.localX, player.localY);

		ScreenUtils.clear(0, 0, 0.0f, 1);
		camera.position.set(player.localX, player.localY, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin();
		renderer.renderState(client.getGameState());
		batch.end();
		if(renderMenu) {
			menu.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	        menu.draw();
		}
		
	}
	
	public void show() {
		this.batch = game.batch;
		camera = new OrthographicCamera();
		this.renderer = new GameStateRenderer(batch, camera, game.assetManager);
		time = System.nanoTime();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		menu = new Stage(new ScreenViewport());
		Table table = new Table();
		table.setFillParent(true);
		Sprite sprite = new Sprite(menuSprite);
		Image image = new Image(sprite);
		image.setColor(1, 1, 1, 0.5f);
		image.setPosition(0, 0);
		menu.addActor(image);
		menu.addActor(table);
		
		table.add(spawn).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(leave).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		showMenu();
	}
}
