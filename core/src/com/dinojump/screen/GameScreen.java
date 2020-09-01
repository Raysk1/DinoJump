package com.dinojump.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.dinojump.entities.DinoEggEntity;
import com.dinojump.entities.FireBall;
import com.dinojump.utilities.WorldCreator;
import com.dinojump.entities.PlayerEntity;
import com.dinojump.utilities.Constants;

public class GameScreen extends BaseScreen {
    private Stage stage;
    private World world;
    private PlayerEntity player;
    private FireBall fireBall;
    private DinoEggEntity eggEntity;
    private Sound dieSound, jumpSound;
    private Music gameMusic;
    private boolean musicOn = true, soundOn = true;
    private String playerTexture = "doux";
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private WorldCreator worldCreator;




    public GameScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(640,384));
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
                    if (soundOn && player.isAlive()) {
                        dieSound.play();
                    }
                    player.setAlive(false);
                    Gdx.input.vibrate(1500);
                    game.switchScreen(game, game.gameOverScreen, stage, 2.5f);

                }
            }

            @Override
            public void endContact(Contact contact) {
                if (areCollided(contact,"player","floor")){
                    player.setJumping(true);

                }

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
        stage.getCamera().position.x = 350;
        playerTexture(playerTexture);
        batch = new SpriteBatch();
        map = game.getManager().get("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map,batch);
        mapRenderer.setView((OrthographicCamera) stage.getCamera());

        worldCreator = new WorldCreator(world,map,stage);
        worldCreator.create();
        fireBall = new FireBall(player);
        stage.addActor(fireBall);


        RectangleMapObject rectangleMapObject = (RectangleMapObject) map.getLayers().get("egg").getObjects().get("egg");
        Rectangle rectangle = rectangleMapObject.getRectangle();
        eggEntity = new DinoEggEntity(rectangle,world,player);
        stage.addActor(eggEntity);


        if (musicOn) {
            gameMusic.play();
        }
    }

    public void hide() {
        stage.clear();
        player.detach();
        eggEntity.detach();
        fireBall.detach();

    }

    @Override
    public void render(float delta) {
        stage.act();
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(delta, 6, 2);


        Vector3 position = stage.getCamera().position;
        float cameraLocationX = (stage.getWidth() / 2.5f) - position.x;
        // float cameraLocationY =  (stage.getHeight() / 2.5f) - position.y;


        float lerp = 0.1f;
        if (player.getX() > 300 && player.getX() > cameraLocationX && player.isAlive()) {
            position.x += (player.getX() + cameraLocationX) * lerp * delta * Constants.PIXELS_IN_METER;
        }
        /*
        if (player.getY() > cameraLocationY){
            position.y += (player.getY() + cameraLocationY) * lerp * delta * Constants.PIXELS_IN_METER;
        }*/

        musicControl();
        mapRenderer.setView((OrthographicCamera) stage.getCamera());
        mapRenderer.render();
        stage.draw();



    }

    @Override
    public void dispose() {
        fireBall.detach();
        mapRenderer.dispose();
        player.detach();
        batch.dispose();
        stage.dispose();
        world.dispose();
        map.dispose();
        worldCreator.detach();

    }


    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    private void playerTexture(String texture) {
        Texture playerTexture = game.getManager().get("DinoSprites - " + texture + ".png");
        player = new PlayerEntity(world, playerTexture, new Vector2(-1, 4f));
        stage.addActor(player);
    }

    public void setPlayerTexture(String playerTexture) {
        this.playerTexture = playerTexture;
    }

    private void musicControl(){

        if (!player.isAlive()) {
            gameMusic.stop();
        }

        if (Gdx.input.justTouched()) {
            if (player.isAlive() && !player.isJumping() && soundOn) {
                jumpSound.play();
            }
            player.setMustJump(true);

        }

    }


}
