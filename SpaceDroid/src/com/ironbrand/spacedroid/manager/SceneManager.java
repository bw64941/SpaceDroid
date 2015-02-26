package com.ironbrand.spacedroid.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.ironbrand.spacedroid.base.BaseScene;
import com.ironbrand.spacedroid.scene.GameScene;
import com.ironbrand.spacedroid.scene.GameoverScene;
import com.ironbrand.spacedroid.scene.IntroScene;
import com.ironbrand.spacedroid.scene.SplashScene;

/**
 * SceneManager Class
 * 
 * @author bwinters
 * 
 */
public class SceneManager {
    private static final SceneManager INSTANCE = new SceneManager();
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    private BaseScene currentScene;
    private Engine engine = ResourcesManager.getInstance().engine;
    private BaseScene splashScene;
    private BaseScene gameScene;
    private BaseScene gameoverScene;
    private BaseScene introScene;

    /**
     * List of all screen types used in the game
     */
    public enum SceneType {
	SCENE_SPLASH, SCENE_INTRO, SCENE_GAME, SCENE_GAMEOVER
    }

    /**
     * Sets the current scene and scene type
     * 
     * @param scene
     */
    public void setScene(BaseScene scene) {
	engine.setScene(scene);
	currentScene = scene;
	currentSceneType = scene.getSceneType();
    }

    /**
     * Sets the current scene type to the scene type passed in
     * 
     * @param sceneType
     */
    public void setScene(SceneType sceneType) {
	switch (sceneType) {
	case SCENE_SPLASH:
	    setScene(splashScene);
	    break;
	case SCENE_INTRO:
	    setScene(introScene);
	    break;
	case SCENE_GAME:
	    setScene(gameScene);
	    break;
	case SCENE_GAMEOVER:
	    setScene(gameoverScene);
	    break;
	default:
	    break;
	}
    }

    /**
     * Returns the singleton instance of SceneManager
     * 
     * @return
     */
    public static SceneManager getInstance() {
	return INSTANCE;
    }

    /**
     * Returns the current scene type
     * 
     * @return
     */
    public SceneType getCurrentSceneType() {
	return currentSceneType;
    }

    /**
     * Returns the current scene
     * 
     * @return
     */
    public BaseScene getCurrentScene() {
	return currentScene;
    }

    /**
     * Entry point from GameActivity. Loads Splash screen graphics and creates
     * Splash Screen
     * 
     * @param pOnCreateSceneCallback
     */
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
	ResourcesManager.getInstance().loadSplashScreen();
	splashScene = new SplashScene();
	currentScene = splashScene;
	pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    /**
     * Entry point from GameActivity. Loads Intro screen graphics and creates
     * Intro Screen
     * 
     * @param pOnCreateSceneCallback
     */
    public void createIntroScene(final Engine mEngine) {
	ResourcesManager.getInstance().unloadSplashScreen();

	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		ResourcesManager.getInstance().loadIntroScreen();
		introScene = new IntroScene();
		setScene(introScene);
	    }
	}));
	disposeSplashScene();
    }

    /**
     * Loads Intro screen graphics and creates Intro Screen after GameoverScene
     * Scene
     * 
     * @param pOnCreateSceneCallback
     */
    public void createIntroSceneAfterGameoverScene(final Engine mEngine) {
	disposeGameoverScene();

	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		ResourcesManager.getInstance().loadIntroScreen();
		introScene = new IntroScene();
		setScene(introScene);
	    }
	}));
    }

    /**
     * Loads game scene graphics and creates Game Scene Disposes Game Over Scene
     * 
     * @param mEngine
     */
    public void createGameSceneAfterGameover(final Engine mEngine) {
	disposeGameoverScene();
	ResourcesManager.getInstance().activity.loadAd();
	ResourcesManager.getInstance().activity.makeAdVisible();
	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		ResourcesManager.getInstance().loadGameResources();
		gameScene = new GameScene();
		setScene(gameScene);
		ResourcesManager.getInstance().gameplayMusic.play();
	    }
	}));

    }

    /**
     * Loads game scene graphics and creates Game Scene Disposes Splash Screen
     * 
     * @param mEngine
     */
    public void createGameScene(final Engine mEngine) {
	ResourcesManager.getInstance().unloadIntroScreen();
	ResourcesManager.getInstance().activity.loadAd();
	ResourcesManager.getInstance().activity.makeAdVisible();
	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		ResourcesManager.getInstance().loadGameResources();
		gameScene = new GameScene();
		setScene(gameScene);
		ResourcesManager.getInstance().gameplayMusic.play();
	    }
	}));
	disposeIntroScene();
    }

    /**
     * Creates the Gameover Scene
     * 
     * @param mEngine
     */
    public void createGameoverScene(final Engine mEngine) {
	disposeGameScene();
	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		ResourcesManager.getInstance().loadGameoverResources();
		gameoverScene = new GameoverScene();
		setScene(gameoverScene);
		ResourcesManager.getInstance().gameplayMusic.stop();
	    }
	}));
	ResourcesManager.getInstance().activity.makeAdInvisible();
    }

    /**
     * Unloads the splash screen graphics and disposes the splash screen
     */
    public void disposeSplashScene() {
	ResourcesManager.getInstance().unloadSplashScreen();
	if (splashScene != null) {
	    splashScene.disposeScene();
	    splashScene = null;
	}
    }

    /**
     * Unloads the intro screen graphics and disposes the intro screen
     */
    public void disposeIntroScene() {
	ResourcesManager.getInstance().unloadIntroScreen();
	if (introScene != null) {
	    introScene.disposeScene();
	    introScene = null;
	}
    }

    /**
     * Unloads the splash screen graphics and disposes the splash screen
     */
    public void disposeGameScene() {
	ResourcesManager.getInstance().unloadGameTextures();
	if (gameScene != null) {
	    gameScene.disposeScene();
	    gameScene = null;
	    ResourcesManager.getInstance().gameplayMusic.stop();
	}
    }

    /**
     * Unloads the splash screen graphics and disposes the splash screen
     */
    public void disposeGameoverScene() {
	ResourcesManager.getInstance().unloadGameoverTextures();
	if (gameoverScene != null) {
	    gameoverScene.disposeScene();
	    gameoverScene = null;
	}
    }

    /**
     * Unloads any remaining scenes and exits the application
     */
    public void exitGame() {
	switch (currentSceneType) {
	case SCENE_SPLASH:
	    disposeSplashScene();
	    break;
	case SCENE_INTRO:
	    disposeIntroScene();
	    break;
	case SCENE_GAME:
	    disposeGameScene();
	    break;
	case SCENE_GAMEOVER:
	    disposeGameoverScene();
	    break;
	default:
	    break;
	}

	System.exit(0);
    }
}
