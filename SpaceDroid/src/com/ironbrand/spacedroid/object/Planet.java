/**
 * 
 */
package com.ironbrand.spacedroid.object;

import java.util.Random;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.ironbrand.spacedroid.GameActivity;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * Planet class
 * 
 * @author bwinters
 * 
 */
public class Planet extends Sprite implements Traversable {

    private static final float X_COORD = 0;
    private static final float Y_COORD = 0;
    private static final float WIDTH = 38;
    private static final float HEIGHT = 38;
    private static float PLANET_X_SPEED = 3f;
    private String description;
    private Random rand = new Random();

    /**
     * Planet Constructor
     */
    public Planet(ITextureRegion texture) {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, texture, ResourcesManager.getInstance().vbom);
	this.setZIndex(7);
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Make the planet traverse across the game scene
     */
    @Override
    public void traverse() {
	int minY = (int) (100);
	int maxY = (int) (GameActivity.CAMERA_HEIGHT); // minu planet width
	int rangeY = maxY - minY;

	int y = rand.nextInt(rangeY);
	int x = (int) (GameActivity.CAMERA_WIDTH + 38);

	this.setPosition(x, y);
	this.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2.0f, 0.0f, 360)));

	MoveXModifier mod = new MoveXModifier(PLANET_X_SPEED, x, -38);
	this.registerEntityModifier(mod.deepCopy());

    }
}
