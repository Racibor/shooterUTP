package shooter.game.scenes;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import shooter.client.ClientNetHandler;
import shooter.game.GameState;
import shooter.server.Server;
import shooter.server.ServerGameController;
import shooter.server.packet.PacketGroup;
import shooter.server.packet.packets.JoinPacket;
import shooter.server.packet.packets.SpawnPacket;


public class ConnectionScene extends ScreenAdapter {
	
	GameAdapter game;
	int port = -1;
	int seed;
	
	ClientNetHandler netHandler;
	
	Executor exec;
	
	BitmapFont font = new BitmapFont(Gdx.files.internal("assets/default.fnt"));
	
	Server server;
	
	PacketGroup packets;
	
	long time;
	
	int timeout = 0;

	
	ConnectionScene(GameAdapter game, int port, InetSocketAddress addr) {
		this.game = game;
		try {
			this.netHandler = new ClientNetHandler(game.parser, port);
			this.netHandler.setAddress(addr);
			this.exec = Executors.newSingleThreadExecutor();
		} catch (IOException e) {
			game.setScreen(new MainScene(game, "error connecting"));
			e.printStackTrace();
		}
	}

	
	ConnectionScene(GameAdapter game, int port, int seed) {
		this.game = game;
		this.port = port;
		this.seed = seed;

	}

	public void dispose() {

	}

	
	public void render(float delta) {
		long cur = System.nanoTime();
		Gdx.gl.glClearColor(0, .25f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		font.draw(game.batch, "Connecting", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		game.batch.end();
		if (cur - time > 500000000) {
			time = cur;
			timeout++;
			if (timeout > 5) {
				game.setScreen(new MainScene(game, "Could not connect to the server"));
			}
			if (netHandler.newUpdate()) {
				PacketGroup update = netHandler.getLastUpdate();
				game.client.applyPackets(update);
				packets.clear();
				if (server == null) {
					game.setScreen(new GameScene(game, netHandler));
				} else {
					game.setScreen(new GameScene(game, netHandler, server));
				}

			}
		}

	}

	
	public void show() {
		if (port > 0) {
			try {
				this.server = new Server("localhost", port, game.parser, new ServerGameController(new GameState(seed)));
				this.netHandler = new ClientNetHandler(game.parser, port + 1);
				game.netHandler = this.netHandler;
				InetSocketAddress addr = new InetSocketAddress("localhost", port);
				this.netHandler.setAddress(addr);
				this.exec = Executors.newSingleThreadExecutor();
				this.exec.execute(server);
			} catch (BindException e) {
				game.setScreen(new MainScene(game, "this port is already taken"));
				return;
			} catch (IOException e) {
				game.setScreen(new MainScene(game, "error connecting"));
				return;
			}
		}
		packets = new PacketGroup(-1);
		packets.insert(new JoinPacket());
		netHandler.queue(packets);
		netHandler.send();
		time = System.nanoTime();

	}
}
