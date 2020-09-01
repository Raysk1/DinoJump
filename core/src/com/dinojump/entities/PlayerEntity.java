package com.dinojump.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dinojump.utilities.Utilities;

import static com.dinojump.utilities.Constants.*;

public class PlayerEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean alive = true, jumping = false, mustJump = false;

    private Animation <TextureRegion> animation;
    private float tiempo;
    private TextureRegion[] playerRunRegion,playerDieRegion;
    private  TextureRegion actualFrame;


    public PlayerEntity(World world, Texture texture, Vector2 position){


        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        //se necesita tdo esto para crear las fisicas de un objeto
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f,0.5f);
        fixture = body.createFixture(shape,3);
        shape.dispose();
        fixture.setUserData("player");

        playerTexture();

        setSize(1.5f* PIXELS_IN_METER, 2* PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * PIXELS_IN_METER);

        //esto actualiza el movimieto de la imagen
        tiempo += Gdx.graphics.getDeltaTime();
        actualFrame = animation.getKeyFrame(tiempo,true);
        batch.draw(actualFrame,getX(),getY(),getWidth(),getHeight());
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);

    }


    @Override
    public void act(float delta) {

        if (mustJump){
            mustJump = false;
           jump();

        }

        if (alive) {
            float velocidadY = body.getLinearVelocity().y;
            body.setLinearVelocity(PLAYER_SPEED, velocidadY);
        }

        if (jumping){
            body.applyForceToCenter(0,-IMPULSE_JUMP * 1.1f, true);
        }

        if (!alive){
            animation =  new Animation<TextureRegion>(0.5f,playerDieRegion);
        }
    }

    public void jump(){
        if (!jumping && alive){
            jumping = true;
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP,position.x,position.y,true);
        }
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }


    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isJumping() {
        return jumping;
    }

    private void playerTexture(){
        playerRunRegion = new TextureRegion[6];
        playerDieRegion = new TextureRegion[3];


        playerRunRegion = Utilities.textureCutter(texture,24,6,17);
        playerDieRegion = Utilities.textureCutter(texture,24,3,14);
        animation = new Animation <TextureRegion>(0.1f,playerRunRegion);
    }
}
