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

import static com.dinojump.Constants.PIXELS_IN_METER;

public class CactusEntity extends Actor {
    private Texture texture;


    public CactusEntity(Vector2 pos, float width, float height,Texture texture){
        this.texture = texture;

        setPosition(pos.x  * com.dinojump.Constants.PIXELS_IN_METER, pos.y * com.dinojump.Constants.PIXELS_IN_METER);
        setSize(width/PIXELS_IN_METER, height/PIXELS_IN_METER);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }



}
