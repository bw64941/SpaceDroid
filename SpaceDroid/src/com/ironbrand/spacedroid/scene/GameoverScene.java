package com.ironbrand.spacedroid.scene;

import org.andengine.entity.sprite.Sprite;

import com.ironbrand.spacedroid.base.BaseScene;
import com.ironbrand.spacedroid.manager.SceneManager;
import com.ironbrand.spacedroid.manager.SceneManager.SceneType;
import com.ironbrand.spacedroid.object.GameoverScore;
import com.ironbrand.spacedroid.scene.buttons.GameoverLeaderButton;
import com.ironbrand.spacedroid.scene.buttons.GameoverRetryButton;
import com.ironbrand.spacedroid.scene.buttons.GameoverSubmitButton;

/**
 * Splash Scene Class Shows company logo, disappears after a few seconds
 * 
 * @author bwinters
 * 
 */
public class GameoverScene extends BaseScene {
    private Sprite gameoverBackground;
    private GameoverRetryButton gameoverRetryButton;
    private GameoverLeaderButton gameoverLeaderButton;
    private GameoverSubmitButton gameoverSubmitButton;

    /**
     * Creates the Splash Scene
     */
    @Override
    public void createScene() {
	gameoverBackground = new Sprite(0, 0, resourcesManager.gameoverBackgroundTexture, vbom);
	gameoverRetryButton = new GameoverRetryButton();
	this.registerTouchArea(gameoverRetryButton);

	gameoverLeaderButton = new GameoverLeaderButton();
	this.registerTouchArea(gameoverLeaderButton);

	gameoverSubmitButton = new GameoverSubmitButton();
	this.registerTouchArea(gameoverSubmitButton);
	
	gameoverBackground.setPosition(400, 240);
	gameoverBackground.attachChild(GameoverScore.getGameoverScore());
	gameoverBackground.attachChild(gameoverRetryButton);
	gameoverBackground.attachChild(gameoverLeaderButton);
	gameoverBackground.attachChild(gameoverSubmitButton);

	this.attachChild(gameoverBackground);
    }

    /**
     * Overrides the back key press
     */
    @Override
    public void onBackKeyPressed() {
	SceneManager.getInstance().exitGame();
    }

    /**
     * Returns the scene type enum
     */
    @Override
    public SceneType getSceneType() {
	return SceneType.SCENE_GAMEOVER;
    }

    /**
     * Disposes scene
     */
    @Override
    public void disposeScene() {
	gameoverRetryButton.detachSelf();
	gameoverLeaderButton.detachSelf();
	gameoverSubmitButton.detachSelf();
	gameoverBackground.detachSelf();
	gameoverRetryButton.dispose();
	gameoverLeaderButton.dispose();
	gameoverSubmitButton.dispose();
	gameoverBackground.dispose();
	GameoverScore.getGameoverScore().detachSelf();
	GameoverScore.getGameoverScore().dispose();
	GameoverScore.setGameoverScore(null);
	this.detachSelf();
	this.dispose();
    }
}