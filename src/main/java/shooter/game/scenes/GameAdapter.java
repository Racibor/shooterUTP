package shooter.game.scenes;

import java.io.IOException;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import shooter.client.ClientGameController;
import shooter.client.ClientNetHandler;
import shooter.game.render.GameStateRenderer;
import shooter.server.packet.PacketParser;
import shooter.server.packet.packets.LeavePacket;


public class GameAdapter extends Game {
	
	
	ClientGameController client;
	
	GameStateRenderer renderer;
	
	OrthographicCamera camera = new OrthographicCamera();
	
	AssetManager assetManager = new AssetManager();
	
	PacketParser parser;
	
	ClientNetHandler netHandler;
	
	SpriteBatch batch;
	
	int tickRate;
	
	private long time;
	
	
	public GameAdapter(ClientGameController client, PacketParser packetParser, int port) throws IOException {
		this.client = client;
		this.tickRate = 24;
		client.setTickRate(24);
		this.parser = packetParser;
		this.netHandler = new ClientNetHandler(parser, port);
	}
	
	public void dispose() {

	}
	
	
	public void create() {
		assetManager.load("assets/skins/defaultSkin.png", Texture.class);
		assetManager.load("assets/skins/bulletSkin.png", Texture.class);
		this.assetManager.finishLoading();
		this.batch = new SpriteBatch();
		setScreen(new MainScene(this));
		
	}
}

