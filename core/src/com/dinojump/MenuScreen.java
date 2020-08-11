package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class MenuScreen extends BaseScreen {
    private Stage stage;
    private Skin skin;
    private Image name;
    private TextButton play, options;

    public MenuScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        name = new Image(game.getManager().get("play.png", Texture.class));
        play = new TextButton("Play", skin);
        options = new TextButton("Options", skin);

        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchScreen(game,game.gameScreen,stage,1f);
            }
        });

        options.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.optionsScreen);
            }
        });

       name.setPosition(320-name.getWidth()/8,320-name.getHeight()/1.5f);
       name.setSize(160,80);
       stage.addActor(name);

        play.setSize(90,40);
        play.setPosition(320-play.getWidth()/2,150);
        stage.addActor(play);


        options.setSize(90,40);
        options.setPosition(320-options.getWidth()/2,100);
        stage.addActor(options);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
