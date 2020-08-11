package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class OptionsScreen extends BaseScreen {
    private Stage stage;
    private Skin skin;
    private Image name;
    private CheckBox music, sound;
    private TextButton back;

    public OptionsScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        music = new CheckBox ("Music",skin);
        back = new TextButton("Back",skin);
        sound = new CheckBox("Sound", skin);

        musicSettings();
        backSettings();
        soundSettings();

        stage.addActor(music);
        stage.addActor(back);
        stage.addActor(sound);

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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f,0.5f,0.8f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        if (music.isChecked()){
            game.gameScreen.setMusicOn(true);
        }else {
            game.gameScreen.setMusicOn(false);
        }

        if (sound.isChecked()){
            game.gameScreen.setSoundOn(true);
        }else {
            game.gameScreen.setSoundOn(false);
        }

        stage.draw();
    }

    private void musicSettings(){
        music.setChecked(true);

        music.setPosition(110,20);
        music.setSize(100,30);
    }

    private void backSettings(){
        back.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchScreen(game,game.menuScreen,stage,.5f);
            }
        });
        back.setPosition(300, 20);
    }

    private void soundSettings() {
       // Gdx.graphics.setContinuousRendering(sound.isChecked());
        sound.setChecked(true);

        sound.setPosition(10,20);
        sound.setSize(100,30);


    }
}
