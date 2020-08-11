package com.dinojump.entities;

/*
 * This file is part of Jump Don't Die
 * Copyright (C) 2015 Dani Rodríguez <danirod@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dinojump.Constants;

public class FloorEntity extends Actor {

    private Texture floor, overfloor;

    private World world;

    private Body body, leftBody;

    private Fixture fixture, leftFixture;


    public FloorEntity(World world, Texture floor, Texture overfloor, float x, float width, float y) {
        this.world = world;
        this.floor = floor;
        this.overfloor = overfloor;


        BodyDef def = new BodyDef();
        def.position.set(x + width / 2, y - 0.5f);
        body = world.createBody(def);

        // Give it a box shape.
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, 0.5f);
        fixture = body.createFixture(box, 1);
        fixture.setUserData("floor");
        box.dispose();

        BodyDef leftDef = new BodyDef();
        leftDef.position.set(x, y - 0.55f);
        leftBody = world.createBody(leftDef);


        PolygonShape leftBox = new PolygonShape();
        leftBox.setAsBox(0.02f, 0.45f);
        leftFixture = leftBody.createFixture(leftBox, 1);
        leftFixture.setUserData("spike");
        leftBox.dispose();

        setSize(width * com.dinojump.Constants.PIXELS_IN_METER, com.dinojump.Constants.PIXELS_IN_METER);
        setPosition(x * com.dinojump.Constants.PIXELS_IN_METER, (y - 1) * Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Render both textures.
        batch.draw(floor, getX(), getY(), getWidth(), getHeight());
        batch.draw(overfloor, getX(), getY() + 0.9f * getHeight(), getWidth(), 0.1f * getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        leftBody.destroyFixture(leftFixture);
        world.destroyBody(leftBody);
        world.destroyBody(body);
    }
}
