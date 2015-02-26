package com.ironbrand.spacedroid.scene;

import org.andengine.entity.sprite.Sprite;

import com.ironbrand.spacedroid.base.BaseScene;
import com.ironbrand.spacedroid.manager.SceneManager;
import com.ironbrand.spacedroid.manager.SceneManager.SceneType;
import com.ironbrand.spacedroid.scene.buttons.IntroPlayButton;
import com.ironbrand.spacedroid.scene.buttons.IntroQuitButton;

/**
 * Intro Scene Class Shows play and quit buttons
 * 
 * @author bwinters
 * 
 */
public class IntroScene extends BaseScene {
    private Sprite introBackground;
    private IntroPlayButton introPlayButton;
    private IntroQuitButton introQuitButton;

    /**
     * Creates the Intro Scene
     */
    @Override
    public void createScene() {
	introBackground = new Sprite(0, 0, resourcesManager.introBackgroundTexture, vbom);
	
	introPlayButton = new IntroPlayButton();
	this.registerTouchArea(introPlayButton);
	introQuitButton = new IntroQuitButton();
	this.registerTouchArea(introQuitButton);
	
	introBackground.setPosition(400, 240);
	introBackground.attachChild(introPlayButton);
	introBackground.attachChild(introQuitButton);
	
	this.attachChild(introBackground);
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
	return SceneType.SCENE_INTRO;
    }

    /**
     * Disposes scene
     */
    @Override
    public void disposeScene() {
	introPlayButton.detachSelf();
	introPlayButton.dispose();
	introQuitButton.detachSelf();
	introQuitButton.dispose();
	introBackground.detachSelf();
	introBackground.dispose();
	this.detachSelf();
	this.dispose();
    }
}