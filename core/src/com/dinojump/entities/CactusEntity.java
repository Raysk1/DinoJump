package com.dinojump.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.dinojump.Constants.PIXELS_IN_METER;

public class CactusEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    public CactusEntity(World world, Texture texture, float x, float y){
        this.texture = texture;
        this.world = world;

        BodyDef def = new BodyDef();
        def.position.set(x,y);
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f,1f);
        fixture = body.createFixture(shape,3);
        shape.dispose();
        fixture.setUserData("cactus");

        setPosition(x  * com.dinojump.Constants.PIXELS_IN_METER, y * com.dinojump.Constants.PIXELS_IN_METER);
        setSize(PIXELS_IN_METER, PIXELS_IN_METER);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }





}
