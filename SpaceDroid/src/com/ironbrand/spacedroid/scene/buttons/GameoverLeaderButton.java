/**
 * 
 */
package com.ironbrand.spacedroid.scene.buttons;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.ironbrand.spacedroid.manager.GameCircleManager;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * Alien Class
 * 
 * @author bwinters
 * 
 */
public class GameoverLeaderButton extends Sprite {

    private static final float X_COORD = 600;
    private static final float Y_COORD = 390;
    private static final float WIDTH = 100;
    private static final float HEIGHT = 76;
    private boolean isLeaderButtonPressed = false;

    /**
     * Alien Constructor
     */
    public GameoverLeaderButton() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().gameoverLeaderButtonTexture, ResourcesManager.getInstance().vbom);
	this.setZIndex(9);
    }

    /**
     * Pre-Draw the graphic
     */
    @Override
    protected void preDraw(GLState pGLState, Camera pCamera) {
	super.preDraw(pGLState, pCamera);
	pGLState.enableDither();
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	if (pSceneTouchEvent.isActionDown()) {
	    this.registerEntityModifier(new ScaleModifier(1.0f, 1.0f, 0.5f));
	}
	if (pSceneTouchEvent.isActionUp()) {
	    this.registerEntityModifier(new ScaleModifier(1.0f, 0.5f, 1.0f));
	    if (isLeaderButtonPressed == false) {
		isLeaderButtonPressed = true;

		GameCircleManager.showLeaderboardOverlay();
	    }
	}
	return true;
    }
}
