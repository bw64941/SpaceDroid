/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.text.Text;

import android.opengl.GLES20;

import com.ironbrand.spacedroid.manager.GameCircleManager;
import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * @author bwinters
 * 
 */
public class GameScore extends Text {

    public static int SCORE_INCREMENT = 10;
    private static final float X_COORD = 120;
    private static final float Y_COORD = 18;
    private int score = 0;
    private static GameScore gameScore;

    /**
     * GameScore Constructor
     */
    private GameScore() {
	super(X_COORD, Y_COORD, ResourcesManager.getInstance().gameScoreFont, "Score: 0", "Score: 0123456789".length(), ResourcesManager.getInstance().vbom);
	this.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	this.setScale(0.5f);
	this.setZIndex(6);
    }

    /**
     * Adds to score
     */
    public void addToScore() {
	score += SCORE_INCREMENT;
	this.setText("Score: " + score);
	GameCircleManager.evaluateAchievements(score);
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
    }

    /**
     * @return the gameScore
     */
    public static GameScore getGameScore() {
	if (gameScore == null) {
	    gameScore = new GameScore();
	}
	return gameScore;
    }

    /**
     * @param gameScore
     *            the gameScore to set
     */
    public static void setGameScore(GameScore gameScore) {
	GameScore.gameScore = gameScore;
    }

}
