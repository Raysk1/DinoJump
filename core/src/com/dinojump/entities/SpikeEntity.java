package com.dinojump.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SpikeEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean isAlive = true, isJumping = false;

    public SpikeEntity(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(x,y);
        body = world.createBody(def);

        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f,-0.5f);
        vertices[1] = new Vector2(0.5f,-0.5f);
        vertices[2] = new Vector2(0,1f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        fixture = body.createFixture(shape,3);
        shape.dispose();
        fixture.setUserData("spike");

        setPosition(x  * com.dinojump.Constants.PIXELS_IN_METER, y * com.dinojump.Constants.PIXELS_IN_METER);
        setSize(com.dinojump.Constants.PIXELS_IN_METER, com.dinojump.Constants.PIXELS_IN_METER);
    }

    public void draw(Batch batch, float parentAlpha) {

        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
