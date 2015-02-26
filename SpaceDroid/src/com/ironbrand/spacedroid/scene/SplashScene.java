package com.ironbrand.spacedroid.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.ironbrand.spacedroid.base.BaseScene;
import com.ironbrand.spacedroid.manager.SceneManager.SceneType;

/**
 * Splash Scene Class Shows company logo, disappears after a few seconds
 * 
 * @author bwinters
 * 
 */
public class SplashScene extends BaseScene {
    private Sprite splash;

    /**
     * Creates the Splash Scene
     */
    @Override
    public void createScene() {
	splash = new Sprite(0, 0, resourcesManager.logo_texture, vbom) {
	    @Override
	    protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		pGLState.enableDither();
	    }
	};

	splash.setPosition(400, 240);
	attachChild(splash);
    }

    /**
     * Overrides the back key press
     */
    @Override
    public void onBackKeyPressed() {
	return;
    }

    /**
     * Returns the scene type enum
     */
    @Override
    public SceneType getSceneType() {
	return SceneType.SCENE_SPLASH;
    }

    /**
     * Disposes scene
     */
    @Override
    public void disposeScene() {
	splash.detachSelf();
	splash.dispose();
	this.detachSelf();
	this.dispose();
    }
}