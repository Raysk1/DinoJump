package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class OptionsScreen extends BaseScreen {
    private Stage stage;
    private Skin skin;
    private Image name;
    private CheckBox audio;

    public OptionsScreen(MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    }
}
