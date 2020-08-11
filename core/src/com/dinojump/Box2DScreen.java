package com.dinojump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class Box2DScreen extends BaseScreen {
    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private Body dinoBody,sueloBody,pinchosBody;

    private Fixture dinoFixture,sueloFixture,pinchosFixture;
    private boolean isDinoJumping, isMustJump, isAlive = true;

    public Box2DScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        world = new World(new Vector2(0,-10),true);
        renderer = new Box2DDebugRenderer();
        //control de la camara
        camera = new OrthographicCamera(16,9);
        camera.translate(0,1);
        dinoBody();

        sueloBody();

        pinchosBody();

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

    private BodyDef createSueloBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0,-1);
        def.type = BodyDef.BodyType.StaticBody;
        return def;
    }

    private BodyDef createDinoBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(-5,0);
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
        world.dispose();
        renderer.dispose();
    }

    @Override
    //metodo que hace todo 30 a 60 veces por seg
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

         playerPhysics();

        world.step(delta,6,2);

        camera.update();
        renderer.render(world,camera.combined);
    }

    private  void sueloBody(){
        //otro objeto (Esta vez estatico)
        BodyDef sueloBodyDef = createSueloBodyDef();
        sueloBody = world.createBody(sueloBodyDef);
        PolygonShape sueloShape = new PolygonShape();
        sueloShape.setAsBox(500,1);
        sueloFixture = sueloBody.createFixture(sueloShape,1);
        sueloShape.dispose();
        sueloFixture.setUserData("floor");

    }

    private void dinoBody(){
        //se necesita tdo esto para crear las fisicas de un objeto
        BodyDef dinoBodyDef = createDinoBodyDef();
        dinoBody = world.createBody(dinoBodyDef);
        PolygonShape dinoShape = new PolygonShape();
        dinoShape.setAsBox(0.5f,0.5f);
        dinoFixture = dinoBody.createFixture(dinoShape,1);
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
        dinoBody.applyLinearImpulse(0,5,position.x,position.y,true);
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
