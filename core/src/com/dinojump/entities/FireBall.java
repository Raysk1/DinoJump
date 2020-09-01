package com.dinojump.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dinojump.utilities.Constants;

import static com.dinojump.utilities.Constants.PIXELS_IN_METER;

public class FireBall extends Actor {
    private Animation <TextureRegion> animation;
    private TextureRegion[] textureRegions = new TextureRegion[5];
    private AssetManager manager;
    private TextureRegion actualFrame;
    private float tiempo;
    private PlayerEntity player;


    public FireBall(PlayerEntity player){
        this.player = player;
        manager = new AssetManager();
        for (int i = 1; i <6 ; i++) {
            manager.load("FB00"+i+".png", Texture.class);

        }

        manager.finishLoading();

        createAnimation();
        setPosition(-1.5f*PIXELS_IN_METER,3);
        setSize(3*PIXELS_IN_METER,1.5f * PIXELS_IN_METER);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getY() < player.getY()-PIXELS_IN_METER/4){
            setPosition(player.getX()-PIXELS_IN_METER*2.5f,getY()+Gdx.graphics.getDeltaTime()*150);
        }

        if (getY() > player.getY()-PIXELS_IN_METER/4){
            setPosition(player.getX()-PIXELS_IN_METER*2.5f,getY()-Gdx.graphics.getDeltaTime()*150);
        }
       // setPosition(player.getX()-PIXELS_IN_METER*2.5f,);

        //esto actualiza el movimieto de la imagen
        tiempo += Gdx.graphics.getDeltaTime();
        actualFrame = animation.getKeyFrame(tiempo,true);

        batch.draw(actualFrame, getX(), getY(), getWidth(), getHeight());


    }



    private void createAnimation(){
        for (int i = 0; i < 5; i++) {
            Texture texture = manager.get("FB00"+(i+1)+".png");
            textureRegions[i] = new TextureRegion();
            textureRegions[i].setRegion(texture);

        }
        animation = new Animation <TextureRegion>(0.15f,textureRegions);
    }

    public void detach(){
        manager.dispose();
    }
}


