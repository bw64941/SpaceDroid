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
import com.ironbrand.spacedroid.object.GameoverScore;

/**
 * Alien Class
 * 
 * @author bwinters
 * 
 */
public class GameoverSubmitButton extends Sprite {

    private static final float X_COORD = 640;
    private static final float Y_COORD = 210;
    private static final float WIDTH = 120;
    private static final float HEIGHT = 91;
    private boolean isSubmitButtonPressed = false;

    /**
     * Alien Constructor
     */
    public GameoverSubmitButton() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().gameoverSubmitButtonTexture, ResourcesManager.getInstance().vbom);
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
	    if (isSubmitButtonPressed == false) {
		isSubmitButtonPressed = true;
		GameCircleManager.submitScoreToLeaderboard(GameoverScore.getGameoverScore().getScore());
	    }
	}
	return true;
    }
}
