package com.dinojump.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.dinojump.utilities.Constants.PIXELS_IN_METER;

public abstract class Entity extends Actor {
    private Texture texture;
    protected Body body;


    public Entity(Body body, float width, float height, Texture texture){
        this.texture = texture;
       this.body = body;

        setPosition(body.getPosition().x*PIXELS_IN_METER , body.getPosition().y*PIXELS_IN_METER);
        setSize(width, height);

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER, (body.getPosition().y - 0.5f) * PIXELS_IN_METER);
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }

}
