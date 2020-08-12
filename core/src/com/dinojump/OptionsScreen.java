package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class OptionsScreen extends BaseScreen {

    private Texture[] texture = new Texture[4];
    private Animation<TextureRegion>[] animation = new Animation[4];
    private float tiempo;
    private TextureRegion[] playerRunRegion[] = new TextureRegion[4][];
    private TextureRegion[] actualFrame = new TextureRegion[4];


    private Stage stage;
    private Skin skin;
    private CheckBox music, sound;
    private TextButton doux,mort,tard,vita;
    private TextButton back;
    private SpriteBatch batch;
    private Texture bg;

    public OptionsScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        music = new CheckBox("Music", skin);
        back = new TextButton("Back", skin);
        sound = new CheckBox("Sound", skin);
        doux = new TextButton("DOUX",skin);
        tard = new TextButton("TARD",skin);
        mort = new TextButton("MORT",skin);
        vita = new TextButton("VITA",skin);

        texture[3] = game.getManager().get("DinoSprites - doux.png");
        texture[1] = game.getManager().get("DinoSprites - mort.png");
        texture[2] = game.getManager().get("DinoSprites - tard.png");
        texture[0] = game.getManager().get("DinoSprites - vita.png");
        bg = game.getManager().get("bg.png");
        batch = new SpriteBatch();

        musicSettings();
        backSettings();
        soundSettings();
        playerTexture();
        playerTextBox();
        playerSelect();

        stage.addActor(music);
        stage.addActor(back);
        stage.addActor(sound);
        stage.addActor(doux);
        stage.addActor(mort);
        stage.addActor(tard);
        stage.addActor(vita);


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
        for (int i = 0; i < 4; i++) {
            texture[i].dispose();
        }

        bg.dispose();
        batch.dispose();
        stage.clear();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        batch.draw(bg, 0, 0, 640, 360);
        tiempo += Gdx.graphics.getDeltaTime();
        for (int i = 0; i < 4; i++) {
            actualFrame[i] = animation[i].getKeyFrame(tiempo, true);
            batch.draw(actualFrame[i], stage.getWidth() / 8 * (2 * +i), 180, 135, 180);

        }

        batch.end();

        stage.act();

        if (music.isChecked()) {
            game.gameScreen.setMusicOn(true);
        } else {
            game.gameScreen.setMusicOn(false);
        }

        if (sound.isChecked()) {
            game.gameScreen.setSoundOn(true);
        } else {
            game.gameScreen.setSoundOn(false);
        }
        stage.draw();
    }

    private void musicSettings() {
        music.setChecked(true);

        music.setPosition(110, 40);
        music.setSize(100, 30);
    }

    private void backSettings() {
        back.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchScreen(game, game.menuScreen, stage, .5f);
            }
        });
        back.setPosition(300, 40);
    }

    private void soundSettings() {
        sound.setChecked(true);


        sound.setPosition(10, 40);
        sound.setSize(100, 30);


    }


    private void playerTexture() {
        for (int i = 0; i < 4; i++) {
            playerRunRegion[i] = Utilities.textureCutter(texture[i], 24, 6, 17);

        }

        for (int i = 0; i < 4; i++) {
            animation[i] = new Animation<TextureRegion>(0.1f, playerRunRegion[i]);

        }
    }

    private void playerTextBox(){

        doux.setPosition(520,130);

        mort.setPosition(210,130);

        tard.setPosition(360,130);

        vita.setPosition(60,130);


    }

    private void playerSelect(){
        vita.setColor(0.7137254901960784f,1,0,1);
        mort.setColor(1,0,0,1);
        tard.setColor(1,0.6823529411764706f,0,1);
        doux.setColor(0,0,0,1);


        doux.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameScreen.setPlayerTexture("doux");
                doux.setColor(0,0,0,1);
                vita.setColor(0.7137254901960784f,1,0,1);
                mort.setColor(1,0,0,1);
                tard.setColor(1,0.6823529411764706f,0,1);

            }
        });

        mort.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameScreen.setPlayerTexture("mort");
                mort.setColor(0,0,0,1);
                doux.setColor(0,0.580392157f,1,1);
                vita.setColor(0.7137254901960784f,1,0,1);
                tard.setColor(1,0.6823529411764706f,0,1);
            }
        });

        tard.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameScreen.setPlayerTexture("tard");
                tard.setColor(0,0,0,1);
                doux.setColor(0,0.580392157f,1,1);
                vita.setColor(0.7137254901960784f,1,0,1);
                mort.setColor(1,0,0,1);

            }
        });

        vita.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameScreen.setPlayerTexture("vita");
                vita.setColor(0,0,0,1);
                doux.setColor(0,0.580392157f,1,1);
                mort.setColor(1,0,0,1);
                tard.setColor(1,0.6823529411764706f,0,1);
            }
        });

    }
}
