/**
 * 
 */
package com.ironbrand.spacedroid.physics;

import java.util.Iterator;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;

/**
 * GamePhysicsWorld class responsible for configuring and setting up physics for
 * the game scene
 * 
 * @author bwinters
 * 
 */
public class GamePhysicsWorld extends PhysicsWorld {

    private static final String TAG = GamePhysicsWorld.class.getName();
    private static final Vector2 GRAVITY_VECTOR = new Vector2(0, -SensorManager.GRAVITY_EARTH * 2);
    private static final boolean ALLOW_SLEEP = false;

    /**
     * GamePhysics World Constructor
     */
    public GamePhysicsWorld() {
	super(GRAVITY_VECTOR, ALLOW_SLEEP);
    }

    /**
     * Clear all physics bodies in scene
     * 
     */
    public void clearPhysicsWorld() {
	Iterator<Joint> allMyJoints = this.getJoints();
	while (allMyJoints.hasNext()) {
	    try {
		final Joint myCurrentJoint = allMyJoints.next();
		this.destroyJoint(myCurrentJoint);
	    } catch (Exception localException) {
//		Log.d(TAG, localException.getMessage());
	    }
	}

	Iterator<Body> localIterator = this.getBodies();
	while (true) {
	    if (!localIterator.hasNext()) {
		this.clearForces();
		this.clearPhysicsConnectors();
		this.reset();
		this.dispose();
		return;
	    }
	    try {
		this.destroyBody(localIterator.next());
	    } catch (Exception localException) {
//		Log.d(TAG, localException.getMessage());
	    }
	}
    }
}
