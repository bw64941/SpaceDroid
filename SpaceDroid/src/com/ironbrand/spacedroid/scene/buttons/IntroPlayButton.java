/**
 * 
 */
package com.ironbrand.spacedroid.scene.buttons;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.ironbrand.spacedroid.manager.ResourcesManager;
import com.ironbrand.spacedroid.manager.SceneManager;

/**
 * Alien Class
 * 
 * @author bwinters
 * 
 */
public class IntroPlayButton extends Sprite {

    private static final float X_COORD = 350;
    private static final float Y_COORD = 90;
    private static final float WIDTH = 176;
    private static final float HEIGHT = 132;
    private boolean isPlayButtonPressed = false;

    /**
     * Alien Constructor
     */
    public IntroPlayButton() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().introPlayButtonTexture, ResourcesManager.getInstance().vbom);
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
	    if (isPlayButtonPressed == false) {
		isPlayButtonPressed = true;
		SceneManager.getInstance().createGameScene(ResourcesManager.getInstance().engine);
	    }
	}
	return true;
    }
}
