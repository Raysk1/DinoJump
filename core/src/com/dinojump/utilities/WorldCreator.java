package com.dinojump.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import static com.dinojump.utilities.Constants.PIXELS_IN_METER;

public class WorldCreator {
    private World world;
    private TiledMap map;
    private Fixture floorFixtures[], spikeFixtures[];
    private Array<Body> floorBodies = new Array<Body>(), spikeBodies = new Array<>();
    private MapObjects mapObjects;
    private Stage stage;

    public WorldCreator(World world, TiledMap map, Stage stage) {
        this.map = map;
        this.world = world;
        this.stage = stage;

    }


    //funcion que crea los bodies  partir de los objetos en el tiledMap

    private void bodyConstruct(String layer, boolean dinamic, Fixture[] fixture, Array<Body> body) {

        mapObjects = map.getLayers().get(layer).getObjects();
        fixture = new Fixture[mapObjects.getCount()];

        for (int i = 0; i < mapObjects.getCount(); i++) {

            RectangleMapObject obj1 = (RectangleMapObject) mapObjects.get(i);
            Rectangle rect = obj1.getRectangle();
            BodyDef def = new BodyDef();
            def.position.set((rect.x + (rect.width / 2)) / PIXELS_IN_METER, (rect.y + (rect.height / 2)) / PIXELS_IN_METER);

            if (dinamic) {
                def.type = BodyDef.BodyType.DynamicBody;
            } else {
                def.type = BodyDef.BodyType.StaticBody;
            }

            body.add(world.createBody(def));
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.width / 2 / (PIXELS_IN_METER), rect.height / 2 / (PIXELS_IN_METER));
            fixture[i] = body.get(i).createFixture(shape, 1);

            shape.dispose();
            fixture[i].setUserData(layer);
        }


    }

    public void bodyConstruct(String layer, boolean dinamic, Fixture fixture,Body body) {

        mapObjects = map.getLayers().get(layer).getObjects();

        RectangleMapObject obj1 = (RectangleMapObject) mapObjects.get(0);
        Rectangle rect = obj1.getRectangle();
        BodyDef def = new BodyDef();
        def.position.set((rect.x + (rect.width / 2)) / PIXELS_IN_METER, (rect.y + (rect.height / 2)) / PIXELS_IN_METER);

        if (dinamic) {
            def.type = BodyDef.BodyType.DynamicBody;
        } else {
            def.type = BodyDef.BodyType.StaticBody;
        }

        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.width / 2 / (PIXELS_IN_METER), rect.height / 2 / (PIXELS_IN_METER));
        fixture = body.createFixture(shape, 1);
        shape.dispose();
        fixture.setUserData(layer);
    }


    public void create() {
        bodyConstruct("floor", false, floorFixtures, floorBodies);
        bodyConstruct("spike", false, spikeFixtures, spikeBodies);

    }


    private void actorTexture(Array<Body> bodies, String layer, boolean dinamic, Texture texture, Fixture[] fixtures) {
        bodyConstruct(layer, dinamic, fixtures, bodies);

        for (int i = 0; i < bodies.size; i++) {

        }

    }


    //funcion que elimina todos los bodies

    public void detach() {
        for (int i = 0; i < floorBodies.size; i++) {
            floorBodies.get(i).destroyFixture(floorFixtures[i]);
            world.destroyBody(floorBodies.get(i));
        }

        for (int i = 0; i < spikeBodies.size; i++) {
            floorBodies.get(i).destroyFixture(spikeFixtures[i]);
            world.destroyBody(spikeBodies.get(i));
        }

    }

}
