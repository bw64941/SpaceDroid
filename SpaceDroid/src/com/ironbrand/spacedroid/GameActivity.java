package com.ironbrand.spacedroid;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;
import com.ironbrand.spacedroid.manager.GameCircleManager;
import com.ironbrand.spacedroid.manager.ResourcesManager;
import com.ironbrand.spacedroid.manager.SceneManager;
import com.ironbrand.spacedroid.manager.SceneManager.SceneType;

/**
 * Main Activity Class for Game
 * 
 * @author bwinters
 * 
 */
public class GameActivity extends BaseGameActivity implements AdListener {

    private static final String TAG = GameActivity.class.getName();
    private static final String AMAZON_AD_APP_KEY = "7c4b8642bed145368741085389ba4dec";
    private AdLayout adView;

    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;
    private BoundCamera camera = null;

    /**
     * General entry point for Game Creation
     */
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
//	Log.d(TAG, "onCreate Called");
	super.onCreate(pSavedInstanceState);
    }

    /**
     * Saving Instance of Game if interrupted.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//	Log.d(TAG, "onSaveInstanceState Called");
	super.onSaveInstanceState(outState);
    }

    /**
     * Creates the Game Engine
     */
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
//	Log.d(TAG, "onCreateEngine Called");
	return new LimitedFPSEngine(pEngineOptions, 60);
    }

    /**
     * Creates the Game Engine Options
     */
    @Override
    public EngineOptions onCreateEngineOptions() {
//	Log.d(TAG, "onCreateEngineOptions Called");
	camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

	final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	engineOptions.getAudioOptions().setNeedsMusic(true);

	return engineOptions;
    }

    /**
     * Creates Game resources
     */
    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
//	Log.d(TAG, "onCreateResources Called");
	// FacebookManager.checkUserLoggedIn();
	ResourcesManager.prepareManager(mEngine, this, this.camera, getVertexBufferObjectManager());
	pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    /**
     * Creates Game scene
     */
    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
//	Log.d(TAG, "onCreateScene Called");

	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    /**
     * After Splash scene is shown, show Intro Scene
     */
    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
//	Log.d(TAG, "onPopulateScene Called");
	mEngine.registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() {
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		mEngine.unregisterUpdateHandler(pTimerHandler);
		SceneManager.getInstance().createIntroScene(mEngine);
	    }
	}));
	pOnPopulateSceneCallback.onPopulateSceneFinished();

    }

    /**
     * Destroys game
     */
    @Override
    protected void onDestroy() {
//	Log.d(TAG, "onDestroy Called");
	super.onDestroy();
    }

    /**
     * Handles back key press
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//	Log.d(TAG, "onKeyDown Called");
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    if (SceneManager.getInstance().getCurrentSceneType() == SceneType.SCENE_GAME) {
		if (ResourcesManager.getInstance().gameplayMusic.isPlaying()) {
		    ResourcesManager.getInstance().gameplayMusic.stop();
		}
	    }
	    SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	}
	return false;
    }

    /**
     * Handles tasks when game is created
     */
    @Override
    public void onGameCreated() {
//	Log.d(TAG, "onGameCreated Called");
	super.onGameCreated();
    }

    /**
     * Handles tasks when game is resumed
     */
    @Override
    public void onResume() {
//	Log.d(TAG, "onResume Called");
	super.onResume();
	GameCircleManager.initializeAmazonGameCirle((Activity) this);
    }

    /**
     * Handles tasks when game is paused
     */
    @Override
    protected void onPause() {
//	Log.d(TAG, "onPause Called");
	super.onPause();
	// Logs 'app deactivate' App Event.
	// AppEventsLogger.deactivateApp(this);
	if (SceneManager.getInstance().getCurrentSceneType() == SceneType.SCENE_GAME) {
	    if (ResourcesManager.getInstance().gameplayMusic.isPlaying()) {
		ResourcesManager.getInstance().gameplayMusic.pause();
	    }
	}
	GameCircleManager.releaseAmazonGameCircle();
	// if (this.mEngine != null && this.mEngine.isRunning()) {
	// this.mEngine.stop();
	// }
	// SceneManager.getInstance().exitGame();
    }

    /**
     * Handles showing adds at bottom of game scene
     */
    @Override
    protected void onSetContentView() {
//	Log.d(TAG, "onSetContentView Called");
	this.mRenderSurfaceView = new RenderSurfaceView(this);
	this.mRenderSurfaceView.setRenderer(this.mEngine, this);
	final FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(super.createSurfaceViewLayoutParams());

	AdRegistration.setAppKey(AMAZON_AD_APP_KEY);

	adView = new AdLayout(this, AdSize.SIZE_320x50);
	adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

	final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
	final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);

	final FrameLayout frameLayout = new FrameLayout(this);
	frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
	frameLayout.addView(adView, adViewLayoutParams);

	this.setContentView(frameLayout, frameLayoutLayoutParams);
    }

    /**
     * Load a new ad.
     */
    public void loadAd() {
	// Log.d(TAG, "LOADING AD");
	AdTargetingOptions adOptions = new AdTargetingOptions();
	adView.loadAd(adOptions);
    }

    /**
     * Sets the Adview to invisible
     */
    public void makeAdInvisible() {
	this.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		adView.setAnimation(AnimationUtils.loadAnimation(ResourcesManager.getInstance().activity, R.anim.disappear));
		adView.setVisibility(AdLayout.GONE);
	    }
	});
    }

    /**
     * Sets the Adview to invisible
     */
    public void makeAdVisible() {
	this.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		adView.setVisibility(AdLayout.VISIBLE);
		adView.setAnimation(AnimationUtils.loadAnimation(ResourcesManager.getInstance().activity, R.anim.fade_in));
	    }
	});
    }

    /**
     * This event is called after a rich media ads has collapsed from an
     * expanded state.
     */
    @Override
    public void onAdCollapsed(AdLayout view) {
	// Log.d(TAG, "Ad collapsed.");
    }

    /**
     * This event is called if an ad fails to load.
     */
    @Override
    public void onAdFailedToLoad(AdLayout view, AdError error) {
	// Log.w(TAG, "Ad failed to load. Code: " + error.getCode() +
	// ", Message: " + error.getMessage());
    }

    /**
     * This event is called once an ad loads successfully.
     */
    @Override
    public void onAdLoaded(AdLayout view, AdProperties adProperties) {
	// Log.d(TAG, adProperties.getAdType().toString() +
	// " Ad loaded successfully.");
    }

    /**
     * This event is called after a rich media ad expands.
     */
    @Override
    public void onAdExpanded(AdLayout view) {
	// Log.d(TAG, "Ad expanded.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	Log.d(TAG, "onActivityResult Called");
	super.onActivityResult(requestCode, resultCode, data);
	// if (Session.getActiveSession() != null) {
	// Session.getActiveSession().onActivityResult(this, requestCode,
	// resultCode, data);
	// }
    }
}
