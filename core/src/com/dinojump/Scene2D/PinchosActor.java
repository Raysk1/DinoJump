package com.dinojump.Scene2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PinchosActor extends Actor {

    private Texture pinchosTexture;
    private int pinchosWidth = 40, pinchosHeight = 50;

    public PinchosActor(Texture pinchosTexture){
        this.pinchosTexture = pinchosTexture;
        setSize(pinchosWidth, pinchosHeight);
    }

    @Override
    //delta es el timpo transcurrido hasta el momento
    public void act(float delta) {
        setPosition(getX()- 100*delta, getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(pinchosTexture,getX(),getY(),pinchosWidth, pinchosHeight);
    }
}
