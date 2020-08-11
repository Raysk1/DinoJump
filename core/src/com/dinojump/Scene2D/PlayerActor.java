package com.dinojump.Scene2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    private Texture playerTexture;
    private boolean alive = true;
    private int dinoWidth = 60, dinoHeigh = 70;

    public PlayerActor(Texture playerTexture){
        this.playerTexture = playerTexture;
        setSize(dinoWidth,dinoHeigh);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
    batch.draw(playerTexture,getX(),getY(),dinoWidth,dinoHeigh);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }
}
