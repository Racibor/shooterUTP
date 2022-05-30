package shooter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.codesnippets4all.json.parsers.JSONParser;
import org.json.JSONObject;
import shooter.client.ClientGameController;
import shooter.game.GameState;
import shooter.game.scenes.GameAdapter;
import shooter.server.packet.PacketParser;
import shooter.server.packet.PacketParserConfiguration;

public class Main {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		PacketParserConfiguration packetParserConfiguration = PacketParserConfiguration.getDefaultConfiguration();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "tst";
		config.width = 800;
		config.height = 600;
		config.foregroundFPS = 50;
		config.backgroundFPS = 50;
		int port = new Random(new Date().getTime()).nextInt()%10000 + 49152;
		ClientGameController client = new ClientGameController(new GameState(port));
		PacketParser packetParser = new PacketParser(packetParserConfiguration);
		GameAdapter ga = new GameAdapter(client, packetParser, port);
		new LwjglApplication(ga, config);

	}

}
