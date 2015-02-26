/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.text.Text;

import android.opengl.GLES20;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * @author bwinters
 * 
 */
public class GameScoreDifference extends Text {

    private static final float X_COORD = 45;
    private static final float Y_COORD = 45;

    /**
     * GameScore Constructor
     */
    public GameScoreDifference() {
	super(X_COORD, Y_COORD, ResourcesManager.getInstance().gameScoreFont, "+", "+XX".length(), ResourcesManager.getInstance().vbom);
	this.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	this.setScale(0.3f);
	this.setZIndex(6);
    }

    /**
     * Show score difference where points were obtained.
     */
    public void showScoreDifference(float x, float y) {
	this.setPosition(x, y);
	this.setText("+" + GameScore.SCORE_INCREMENT);
	this.setVisible(true);
	this.registerEntityModifier(new ParallelEntityModifier(new RotationModifier(1.0f, 0, 360.0f), new ScaleModifier(1.0f, 0.0f, 0.5f)));
    }

    /**
     * Fade the score difference text
     */
    public void fade() {
	this.setVisible(false);
    }
}
