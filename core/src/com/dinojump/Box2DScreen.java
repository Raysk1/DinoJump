package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.dinojump.Constants.PIXELS_IN_METER;

public class Box2DScreen extends BaseScreen {
    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private Body dinoBody,sueloBody,pinchosBody;

    private Fixture dinoFixture,sueloFixture,pinchosFixture;
    private boolean isDinoJumping, isMustJump, isAlive = true;
    SpriteBatch batch;
    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    private WorldCreator bodyCreator;

    public Box2DScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        world = new World(new Vector2(0,-10),true);
        renderer = new Box2DDebugRenderer();
        //control de la camara
        camera = new OrthographicCamera(32,18);
        camera.translate(0,0);
        dinoBody();

        batch = new SpriteBatch();
        map = game.getManager().get("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map,1,batch);
        mapRenderer.setView(camera);

        bodyCreator = new WorldCreator(world,map);
        bodyCreator.create();
        //sueloBody();

        //pinchosBody();

        contactListener();


    }



    private Fixture createPinchosFixture(Body pinchosBody){
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f,-0.5f);
        vertices[1] = new Vector2(0.5f,-0.5f);
        vertices[2] = new Vector2(0,0.5f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        Fixture fix = pinchosBody.createFixture(shape,1);
        shape.dispose();
        return fix;
    }

    private BodyDef createPinchosBodyDef(float x) {
        BodyDef def = new BodyDef();
        def.position.set(x,0.5f);
        def.type = BodyDef.BodyType.StaticBody;
        return def;
    }

    private BodyDef createSueloBodyDef(float x, float y) {
        BodyDef def = new BodyDef();
        //def.position.set(x/PIXELS_IN_METER*2,y/PIXELS_IN_METER*2);
        def.type = BodyDef.BodyType.StaticBody;

        System.out.println(y);
        return def;
    }

    private BodyDef createDinoBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0,1);
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    @Override
    //cada vez que creas algo lo tienes que destruir
    public void dispose() {
        sueloBody.destroyFixture(sueloFixture);
        dinoBody.destroyFixture(dinoFixture);
        pinchosBody.destroyFixture(pinchosFixture);
        world.destroyBody(dinoBody);
        world.destroyBody(sueloBody);
        world.destroyBody(pinchosBody);
        bodyCreator.detach();
        world.dispose();
        renderer.dispose();
    }

    @Override
    //metodo que hace todo 30 a 60 veces por seg
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

         playerPhysics();

        world.step(delta,6,2);

        //mapRenderer.setView(camera);

        camera.update();
        renderer.render(world,camera.combined);
    }

    private  void sueloBody(){







        MapObjects mapObjects = map.getLayers().get("floor").getObjects();

        for (int i = 0; i <mapObjects.getCount() ; i++) {

        RectangleMapObject obj1 = (RectangleMapObject) mapObjects.get(i);
        Rectangle rect = obj1.getRectangle();
            BodyDef def = new BodyDef();
            def.position.set((rect.x*2)/PIXELS_IN_METER,(rect.y*2)/PIXELS_IN_METER);
            def.type = BodyDef.BodyType.StaticBody;

            System.out.println(rect.x);

            sueloBody = world.createBody(def);
            PolygonShape sueloShape = new PolygonShape();
            sueloShape.setAsBox(rect.width/ (PIXELS_IN_METER),rect.height/ (PIXELS_IN_METER));
            sueloFixture = sueloBody.createFixture(sueloShape,1);
            sueloShape.dispose();
            sueloFixture.setUserData("floor");
        }








        //otro objeto (Esta vez estatico)


    }

    private void dinoBody(){
        //se necesita tdo esto para crear las fisicas de un objeto
        BodyDef dinoBodyDef = createDinoBodyDef();
        dinoBody = world.createBody(dinoBodyDef);
        PolygonShape dinoShape = new PolygonShape();
        dinoShape.setAsBox(0.5f,0.5f);
        dinoFixture = dinoBody.createFixture(dinoShape,3);
        dinoShape.dispose();
        dinoFixture.setUserData("player");

    }

    private void pinchosBody(){
        BodyDef pinchosBodyDef = createPinchosBodyDef(5);
        pinchosBody = world.createBody(pinchosBodyDef);
        pinchosFixture = createPinchosFixture(pinchosBody);
        pinchosFixture.setUserData("spike");
    }

    private void contactListener(){
        world.setContactListener(new ContactListener() {
            @Override

            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor") ||
                        fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player")){
                    if (Gdx.input.isTouched()){
                        isMustJump = true;
                    }
                    isDinoJumping = false;
                }

                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("spike") ||
                        fixtureA.getUserData().equals("spike") && fixtureB.getUserData().equals("player")){
                    isAlive = false;
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if (fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor") ||
                        fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player")){
                    isDinoJumping = true;

                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private void jump(){
        Vector2 position = dinoBody.getPosition();
        dinoBody.applyLinearImpulse(0,25,position.x,position.y,true);
    }

    private void playerPhysics(){
        if (isMustJump){
            isMustJump = false;
            jump();
        }

        if (Gdx.input.justTouched() && !isDinoJumping){
            isMustJump = true;

        }

        if (isAlive) {
            float velocidadY = dinoBody.getLinearVelocity().y;
            dinoBody.setLinearVelocity(2, velocidadY);
        }
    }
}
