/**
 * 
 */
package com.ironbrand.spacedroid.physics.object;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * TopWall class creates boundary walls for game scene
 * 
 * @author bwinters
 * 
 */
public class LeftWall extends Rectangle {
    public static final short CATEGORYBIT_LEFTWALL = 1;
    private static final short MASKBITS_LEFTWALL = Spaceship.CATEGORYBIT_SPACESHIP;
    private static final FixtureDef LEFTWALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f, false, CATEGORYBIT_LEFTWALL, MASKBITS_LEFTWALL, (short) 0);
    private static final float X_COORD = 0;
    private static final float Y_COORD = 240;
    private static final float WIDTH = 2;
    private static final float HEIGHT = 480;

    /**
     * TopWall Constructor
     */
    public LeftWall() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().vbom);
	this.setColor(Color.BLACK);
	this.setZIndex(4);
	createPhysics();
    }

    /**
     * Creates the physics properties for the wall
     */
    private void createPhysics() {
	PhysicsFactory.createBoxBody(ResourcesManager.getInstance().gamePhysicsWorld, this, BodyType.StaticBody, LEFTWALL_FIXTURE_DEF);
    }
}
