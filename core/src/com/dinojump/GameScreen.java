package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.dinojump.entities.CactusEntity;
import com.dinojump.entities.FloorEntity;
import com.dinojump.entities.PlayerEntity;
import com.dinojump.entities.SpikeEntity;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {
    private Stage stage;
    private World world;
    private com.dinojump.entities.PlayerEntity player;
    private List<FloorEntity> floorList = new ArrayList<>();
    private List<SpikeEntity> spikeList = new ArrayList<>();
    private List<CactusEntity> cactusList = new ArrayList<>();
    private Sound dieSound, jumpSound;
    private Music gameMusic;
    private boolean musicOn = true, soundOn = true;
    private String playerTexture = "doux";


    public GameScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(640, 360));
        world = new World(new Vector2(0, -10), true);
        dieSound = game.getManager().get("audio/die.ogg");
        jumpSound = game.getManager().get("audio/jump.ogg");
        gameMusic = game.getManager().get("audio/song.ogg");

        world.setContactListener(new ContactListener() {

            private boolean areCollided(Contact contact, Object userA, Object userB) {
                return (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB) ||
                        contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
            }

            @Override
            public void beginContact(Contact contact) {
                if (areCollided(contact, "player", "floor")) {
                    player.setJumping(false);
                    if (Gdx.input.isTouched()) {
                        player.setMustJump(true);
                        if (player.isAlive() && soundOn) {
                            jumpSound.play();
                        }
                    }
                }

                if (areCollided(contact, "player", "spike")) {
                    player.setAlive(false);
                    if (soundOn) {
                        dieSound.play();
                    }
                    Gdx.input.vibrate(2000);
                    game.switchScreen(game, game.gameOverScreen, stage, 2.5f);
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }


        });

    }

    @Override
    public void show() {
        playerTexture(playerTexture);


        Texture floorTexture = game.getManager().get("floor_1.png");
        Texture overFloorTexture = game.getManager().get("overfloor.png");
        floorList.add(new com.dinojump.entities.FloorEntity(world, floorTexture, overFloorTexture, -10, 500, 1));
        floorList.add(new com.dinojump.entities.FloorEntity(world, floorTexture, overFloorTexture, 30, 5, 2f));
        for (com.dinojump.entities.FloorEntity floor : floorList) {
            stage.addActor(floor);
        }


        Texture spikeTexture = game.getManager().get("spike.png");
        spikeList.add(new com.dinojump.entities.SpikeEntity(world, spikeTexture, 32, 2));
        spikeList.add(new com.dinojump.entities.SpikeEntity(world, spikeTexture, 40, 1));
        spikeList.add(new com.dinojump.entities.SpikeEntity(world, spikeTexture, 45, 1));
        spikeList.add(new com.dinojump.entities.SpikeEntity(world, spikeTexture, 50, 1));
        spikeList.add(new com.dinojump.entities.SpikeEntity(world, spikeTexture, 20, 1));
        for (com.dinojump.entities.SpikeEntity spike : spikeList) {
            stage.addActor(spike);
        }

        Texture cactusTexture = game.getManager().get("Cactus.png");
        cactusList.add(new CactusEntity(world, cactusTexture, 55, 1));
        cactusList.add(new CactusEntity(world, cactusTexture, 56, 1));
        cactusList.add(new CactusEntity(world, cactusTexture, 57, 1));
        cactusList.add(new CactusEntity(world, cactusTexture, 58, 1));
        for (CactusEntity cactus : cactusList) {
            stage.addActor(cactus);
        }
        if (musicOn) {
            gameMusic.play();
        }
    }

    public void hide() {
        stage.clear();
        player.detach();

        for (FloorEntity floor : floorList) {
            floor.detach();
        }
        for (SpikeEntity spike : spikeList) {
            spike.detach();
        }

        for (CactusEntity cactus : cactusList) {
            cactus.detach();
        }

        floorList.clear();
        spikeList.clear();
        cactusList.clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        world.step(delta, 6, 2);


        Vector3 position = stage.getCamera().position;
        float cameraLocation = (stage.getWidth() / 2.5f) - position.x;
        if (player.getX() > cameraLocation && player.isAlive()) {
            float lerp = 0.1f;

            position.x += (player.getX() + cameraLocation) * lerp * delta * Constants.PIXELS_IN_METER;
        }

        if (!player.isAlive()) {
            gameMusic.stop();
        }

        if (Gdx.input.justTouched()) {
            if (player.isAlive() && !player.isJumping() && soundOn) {
                jumpSound.play();
            }
            player.jump();

        }

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }


    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    private  void playerTexture(String texture){
        Texture playerTexture = game.getManager().get("DinoSprites - "+texture+".png");
        player = new PlayerEntity(world, playerTexture, new Vector2(-1, 1.5f));
        stage.addActor(player);
    }

    public void setPlayerTexture(String playerTexture) {
        this.playerTexture = playerTexture;
    }

    public String getPlayerTexture() {
        return playerTexture;
    }

    public boolean isMusicOn() {
        return musicOn;
    }


}
