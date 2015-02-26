/**
 * 
 */
package com.ironbrand.spacedroid.object;

import java.util.Random;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;

import com.ironbrand.spacedroid.GameActivity;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * Alien Class
 * 
 * @author bwinters
 * 
 */
public class Alien extends Sprite implements Traversable {

    private static final float X_COORD = 0;
    private static final float Y_COORD = 0;
    private static final float WIDTH = 45;
    private static final float HEIGHT = 45;
    private static final float SCALE_FACTOR = 0.5f;
    private static float ALIEN_X_SPEED = 2f;
    private Random rand = new Random();

    /**
     * Alien Constructor
     */
    public Alien() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().alienTexture, ResourcesManager.getInstance().vbom);
	this.setScale(Alien.SCALE_FACTOR);
	this.setZIndex(7);
    }

    @Override
    public void traverse() {
	int minY = 66;
	int maxY = (int) (GameActivity.CAMERA_HEIGHT);
	int rangeY = maxY - minY;
	int y = rand.nextInt(rangeY) + minY;
	int x = (int) (GameActivity.CAMERA_WIDTH + 45);

	this.setPosition(x, y);
	this.registerEntityModifier(new LoopEntityModifier(new RotationModifier(3.0f, 360.0f, 0.0f)));

	MoveXModifier mod = new MoveXModifier(ALIEN_X_SPEED, x, -45);
	this.registerEntityModifier(mod.deepCopy());
    }
}
