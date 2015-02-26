/**
 * 
 */
package com.ironbrand.spacedroid.physics.object;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.touch.TouchEvent;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * Spaceship class handles main player logic for game
 * 
 * @author bwinters
 * 
 */
public class Spaceship extends AnimatedSprite {
    public static final short CATEGORYBIT_SPACESHIP = 2;
    private static final String TAG = Spaceship.class.getName();
    private static final float X_COORD = 100;
    private static final float Y_COORD = 140;
    private static final float WIDTH = 103;
    private static final float HEIGHT = 38;
    private static final short MASKBITS_SPACESHIP = TopWall.CATEGORYBIT_TOPWALL + LeftWall.CATEGORYBIT_LEFTWALL + Asteroid.CATEGORYBIT_ASTEROID + BottomWall.CATEGORYBIT_BOTTOMWALL;
    private static final FixtureDef SPACESHIP_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0.5f, 0.5f, 0.5f, false, CATEGORYBIT_SPACESHIP, MASKBITS_SPACESHIP, (short) 0);
    private static final Vector2 ANTI_GRAVITY = new Vector2(0, SensorManager.GRAVITY_EARTH);
    private Body body;

    /**
     * Spaceship Constructor
     */
    public Spaceship() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().playerTexture, ResourcesManager.getInstance().vbom);
	createPhysics();
	this.setZIndex(4);
	ResourcesManager.getInstance().camera.setChaseEntity(this);
    }

    /**
     * Create player physics
     */
    private void createPhysics() {
	body = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().gamePhysicsWorld, this, BodyType.DynamicBody, SPACESHIP_FIXTURE_DEF);
	body.setUserData("player");
	this.animate(500);

	ResourcesManager.getInstance().gamePhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
    }

    /**
     * Floats the player up in gravity
     * 
     * @param pSceneTouchEvent
     */
    public void floatUp(final TouchEvent pSceneTouchEvent) {
	Vector2 antiGravity = ANTI_GRAVITY;
	antiGravity.y = (float) (SensorManager.GRAVITY_EARTH);
	antiGravity.x = 0;

	body.setLinearVelocity(antiGravity);
    }
    
    /**
     * Floats player side to side
     * @param pAccelerationData
     */
    public void swaySideToSide(AccelerationData pAccelerationData) {
	final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), 0);
	body.setLinearVelocity(gravity);
    }

    /**
     * Executes when spaceship dies
     */
    public void onDie() {
//	Log.d(TAG, "DEAD");
	this.setVisible(false);
	this.setIgnoreUpdate(true);
    }
    
    /**
     * Executes when spaceship is hit by enemy
     */
    public void onHit() {
	this.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1.0f, 0.0f, 360)));
	body.setLinearVelocity(-5.0f, body.getLinearVelocity().y);
    }
}
