package shooter.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class MainScene extends ScreenAdapter {
	
	GameAdapter game;
	
	Stage stage;
	
	String msg;
	Skin skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
	BitmapFont font = new BitmapFont(Gdx.files.internal("assets/default.fnt"));
	
	final TextButton join = new TextButton("Join", skin);
	
	final TextButton create = new TextButton("Create", skin);
	
	public MainScene(GameAdapter game) {
		this.game = game;
		this.msg = "";
		stage = new Stage(new ScreenViewport());
		
		initListeners();
	}
	
	public MainScene(GameAdapter game, String msg) {
		this.game = game;
		stage = new Stage(new ScreenViewport());
		this.msg = msg;
		initListeners();
	}
	
	public void initListeners() {
		join.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new JoinScene(game));
				
			}
		});
		
		create.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new CreateScene(game));
				
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
        
        game.batch.begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        font.draw(game.batch, msg, stage.getViewport().getScreenX(), stage.getViewport().getScreenY() + 20);
        game.batch.end();
        
	}
	
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.add(join).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(create).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
	}
}
