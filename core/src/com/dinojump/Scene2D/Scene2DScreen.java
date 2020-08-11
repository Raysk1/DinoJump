package com.dinojump.Scene2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dinojump.BaseScreen;
import com.dinojump.MainGame;

public class Scene2DScreen extends BaseScreen {

    private Stage stage;
    private PlayerActor player;
    private PinchosActor pinchos;
    private Texture playerTexture, pinchosTexture;

    public Scene2DScreen(MainGame game) {
        super(game);
        playerTexture = new Texture("dino_1.png");

        pinchosTexture = new Texture("worm_1.png");
    }


    @Override
    //para mostrar todos los elementos del stage
    public void show() {

        stage = new Stage();
        stage.setDebugAll(true);


        player = new PlayerActor(playerTexture);
        pinchos = new PinchosActor(pinchosTexture);
        stage.addActor(player);
        stage.addActor(pinchos);

        pinchos.setPosition(600,100);
        player.setPosition(0,100);
    }

    @Override
    public void hide() {
        stage.dispose();
        playerTexture.dispose();
        pinchosTexture.dispose();
    }

    @Override
    //Dibuja el stage
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f,0.5f,0.8f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }


}
