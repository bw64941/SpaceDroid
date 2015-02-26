/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.sprite.Sprite;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * HealthBarBackground class
 * 
 * @author bwinters
 * 
 */
public class HealthBarBackground extends Sprite {
    private static final float X_COORD = 650;
    private static final float Y_COORD = 22;
    private static final float WIDTH = 210;
    private static final float HEIGHT = 38;

    /**
     * HealthBarBackground Constructor
     */
    public HealthBarBackground() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().healthTexture, ResourcesManager.getInstance().vbom);
	this.setZIndex(5);
    }
}
