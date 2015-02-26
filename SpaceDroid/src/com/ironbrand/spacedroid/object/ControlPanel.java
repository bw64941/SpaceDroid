/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.sprite.Sprite;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * ControlPanel Class
 * 
 * @author bwinters
 * 
 */
public class ControlPanel extends Sprite {
    private static final float X_COORD = 400;
    private static final float Y_COORD = 21;
    private static final float WIDTH = 800;
    private static final float HEIGHT = 42;

    /**
     * ControlPanel Constructor
     */
    public ControlPanel() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().controlPanel, ResourcesManager.getInstance().vbom);
	this.setZIndex(4);
    }
}
