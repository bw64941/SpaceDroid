/**
 * 
 */
package com.ironbrand.spacedroid.physics.object;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * Asteroid class
 * 
 * @author bwinters
 * 
 */
public class Asteroid extends Sprite {
    public static final short CATEGORYBIT_ASTEROID = 3;
    private static final float X_COORD = 0;
    private static final float Y_COORD = 0;
    private static final float WIDTH = 35;
    private static final float HEIGHT = 35;
    private static final short MASKBITS_ASTEROID = Spaceship.CATEGORYBIT_SPACESHIP;
    private static final FixtureDef ASTEROID_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f, false, CATEGORYBIT_ASTEROID, MASKBITS_ASTEROID, (short) 0);
    private Body body;

    /**
     * Asteroid Constructor
     */
    public Asteroid() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().asteroidTexture, ResourcesManager.getInstance().vbom);
	this.setZIndex(7);
	createPhysics();
    }

    /**
     * Creates the physics for asteroid
     * 
     * @param camera
     */
    private void createPhysics() {
	body = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().gamePhysicsWorld, this, BodyType.KinematicBody, ASTEROID_FIXTURE_DEF);
	body.setUserData("asteroid");

	this.registerUpdateHandler(new PhysicsHandler(this));

	ResourcesManager.getInstance().gamePhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
    }

    /**
     * @return the body
     */
    public Body getBody() {
	return body;
    }
}
