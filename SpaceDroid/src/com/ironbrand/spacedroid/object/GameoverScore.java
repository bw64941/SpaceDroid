/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.text.Text;

import android.opengl.GLES20;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * @author bwinters
 * 
 */
public class GameoverScore extends Text {

    private static final float X_COORD = 400;
    private static final float Y_COORD = 240;
    private int score = 0;
    public static GameoverScore gameoverScore;

    /**
     * GameScore Constructor
     */
    private GameoverScore() {
	super(X_COORD, Y_COORD, ResourcesManager.getInstance().gameoverScoreFont, 0 + "", "0123456789".length(), ResourcesManager.getInstance().vbom);
	this.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//	this.setScale(0.5f);
	this.setZIndex(10);
    }

    /**
     * @return the score
     */
    public int getScore() {
	return score;
    }

    /**
     * @param score
     *            the score to set
     */
    public void setScore(int score) {
	this.score = score;
	this.setText(this.score + "");
    }

    /**
     * @param gameoverScore
     *            the gameoverScore to set
     */
    public static void setGameoverScore(GameoverScore gameoverScore) {
	GameoverScore.gameoverScore = gameoverScore;
    }

    /**
     * @return the gameoverScore
     */
    public static GameoverScore getGameoverScore() {
	if (gameoverScore == null) {
	    gameoverScore = new GameoverScore();
	}
	return gameoverScore;
    }

}