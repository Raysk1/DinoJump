package com.dinojump.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dinojump.utilities.Constants;
import com.dinojump.utilities.Utilities;

import static com.dinojump.utilities.Constants.EGG_SPEED;
import static com.dinojump.utilities.Constants.PIXELS_IN_METER;

public class DinoEggEntity extends Actor {
    private AssetManager manager;
    private TextureRegion[] textureRegions;
    private TextureRegion actualFrame;
    private Animation<TextureRegion> animation;
    private float tiempo;
    private Fixture fixture;
    private Body body;
    private Rectangle rectangle;
    private World world;
    private PlayerEntity player;

    public DinoEggEntity(Rectangle rectangle, World world,PlayerEntity player) {
        this.world = world;
        this.rectangle = rectangle;
        this.player = player;
        manager = new AssetManager();
        manager.load("egg.png", Texture.class);
        manager.finishLoading();
        createBody();
        createAnimation();

        setSize(64, 64);

    }

    @Override
    public void act(float delta) {
        if (getX() - player.getX() < 20* PIXELS_IN_METER){
            body.setLinearVelocity(-EGG_SPEED,body.getLinearVelocity().y);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        System.out.println(body.getPosition().x);
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER,
                (body.getPosition().y - .62f) * PIXELS_IN_METER);

        //esto actualiza el movimieto de la imagen
        tiempo += Gdx.graphics.getDeltaTime();

        actualFrame = animation.getKeyFrame(tiempo, true);

        batch.draw(actualFrame, getX(),getY(), getWidth(), getHeight());

    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
        manager.dispose();
    }

    public void createAnimation() {
        Texture texture = manager.get("egg.png");
        textureRegions = Utilities.textureCutter(texture,4,4,0);
        for (int i = 0; i <4 ; i++) {
            textureRegions[i].flip(true,false);
        }
        animation = new Animation<>(0.15f, textureRegions);
    }

    public void createBody() {
        BodyDef def = new BodyDef();
        def.position.set((rectangle.x + (rectangle.width / 2)) / PIXELS_IN_METER, (rectangle.y + (rectangle.height / 2)) / PIXELS_IN_METER);


        def.type = BodyDef.BodyType.DynamicBody;


        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.width / 2 / (PIXELS_IN_METER), rectangle.height / 2 / (PIXELS_IN_METER));
        fixture = body.createFixture(shape, 1);
        shape.dispose();
        fixture.setUserData("egg");
    }

}
