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
public class BottomWall extends Rectangle {
    public static final short CATEGORYBIT_BOTTOMWALL = 2;
    private static final short MASKBITS_BOTTOMWALL = Spaceship.CATEGORYBIT_SPACESHIP;
    private static final FixtureDef BOTTOMWALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0.5f, 0.5f, 0.5f, false, CATEGORYBIT_BOTTOMWALL, MASKBITS_BOTTOMWALL, (short) 0);
    private static final float X_COORD = 400;
    private static final float Y_COORD = 10;
    private static final float WIDTH = 800;
    private static final float HEIGHT = 2;

    /**
     * TopWall Constructor
     */
    public BottomWall() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().vbom);
	this.setColor(Color.BLACK);
	this.setZIndex(4);
	createPhysics();
    }

    /**
     * Creates the physics properties for the wall
     */
    private void createPhysics() {
	PhysicsFactory.createBoxBody(ResourcesManager.getInstance().gamePhysicsWorld, this, BodyType.StaticBody, BOTTOMWALL_FIXTURE_DEF);
    }
}
