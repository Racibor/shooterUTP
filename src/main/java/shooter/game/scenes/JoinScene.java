package shooter.game.scenes;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class JoinScene extends ScreenAdapter {
	
	GameAdapter game;
	
	Stage stage;
	
	Skin skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
	
	final TextButton join = new TextButton("Join", skin);
	
	final TextField address = new TextField("127.0.0.1", skin);
	
	final TextField port = new TextField("2000", skin);
	
	final TextButton back = new TextButton("Back", skin);
	
	
	public JoinScene(GameAdapter game) {
		this.game = game;
		stage = new Stage(new ScreenViewport());
		
		initListeners();
	}
	
	private void initListeners() {
		join.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
				Pattern ipPattern = Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
				int clientPort = 1000 + Math.abs(new Random().nextInt()%2000);
				
				String addr = address.getText();
				if(!ipPattern.matcher(addr).matches()) {
					game.setScreen(new MainScene(game, "incorrect address"));
					return;
				}
				String serverPort = port.getText();
				if(!pattern.matcher(serverPort).matches()) {
					game.setScreen(new MainScene(game, "incorrect server Port"));
					return;
				}
				InetSocketAddress add = new InetSocketAddress(addr, Integer.valueOf(serverPort)); 
				game.setScreen(new ConnectionScene(game, clientPort, add));
			}
		});
		
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new MainScene(game));
				
			}
		});
	}

	public void dispose() {
		stage.dispose();
	}
	
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        
	}
	
	
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.add(join).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(address).fillX().uniformX();
		table.add(port).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(back).fillX().uniformX();
	}
}
