package com.dinojump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class MainGame extends Game {
	private AssetManager manager;
	public com.dinojump.GameScreen gameScreen;
	public com.dinojump.GameOverScreen gameOverScreen;
	public com.dinojump.MenuScreen menuScreen;
	public OptionsScreen optionsScreen;

	public AssetManager getManager(){
		return manager;
	}

	@Override
	public void create() {
		manager = new AssetManager();
		manager.load("DinoSprites - doux.png",Texture.class);
		manager.load("spike.png",Texture.class);
		manager.load("Cactus.png",Texture.class);
		manager.load("floor_1.png",Texture.class);
		manager.load("overfloor.png",Texture.class);
		manager.load("audio/die.ogg", Sound.class);
		manager.load("audio/jump.ogg", Sound.class);
		manager.load("audio/song.ogg", Music.class);
		manager.load("gameover.png", Texture.class);
		manager.load("play.png", Texture.class);
		manager.finishLoading();

		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		menuScreen = new MenuScreen(this);
		optionsScreen = new OptionsScreen(this);

		setScreen(optionsScreen);
	}

	public void switchScreen(final Game game, final Screen newScreen, Stage stage, float delay){

		SequenceAction sequenceAction = new SequenceAction();
		sequenceAction.addAction(Actions.fadeOut(delay));
		sequenceAction.addAction(run(new Runnable() {
			@Override
			public void run() {
				game.setScreen(newScreen);
			}
		}));
		sequenceAction.addAction(Actions.fadeIn(0.1f));
		stage.getRoot().addAction(sequenceAction);
	}





}
