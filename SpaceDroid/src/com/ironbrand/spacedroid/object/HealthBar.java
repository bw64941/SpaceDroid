/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.adt.color.Color;

import com.ironbrand.spacedroid.manager.ResourcesManager;
import com.ironbrand.spacedroid.scene.GameScene;

/**
 * HealthBar Class
 * 
 * @author bwinters
 * 
 */
public class HealthBar extends Rectangle {

    private static final float X_COORD = 650;
    private static final float Y_COORD = 22;
    private static final float WIDTH = GameScene.MAX_HEALTH;
    private static final float HEIGHT = 30;

    /**
     * HealthBar Constructor
     */
    public HealthBar() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().vbom);
	this.setColor(Color.GREEN);
	this.setWidth(GameScene.MAX_HEALTH);
	this.setAlpha(0.5f);
	this.setZIndex(6);
    }
}
