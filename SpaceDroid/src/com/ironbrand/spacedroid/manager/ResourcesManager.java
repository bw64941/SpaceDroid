/**
 * 
 */
package com.ironbrand.spacedroid.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import com.ironbrand.spacedroid.GameActivity;
import com.ironbrand.spacedroid.physics.GamePhysicsWorld;

/**
 * Resource Manager
 * 
 * @author bwinters
 * 
 */
public class ResourcesManager {

    private static final String TAG = ResourcesManager.class.getName();
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    public Engine engine = null;
    public GameActivity activity = null;
    public BoundCamera camera = null;
    public VertexBufferObjectManager vbom = null;
    public GamePhysicsWorld gamePhysicsWorld = null;

    // Font for scoring
    public Font gameScoreFont = null;
    public Font gameoverScoreFont = null;

    // Music for gameplay
    public Music gameplayMusic = null;

    // Game Scene Atlas & Texture
    public BuildableBitmapTextureAtlas gameTextureAtlas = null;
    public ITextureRegion backgroundTextureRegion = null;
    public ITextureRegion backgroundStarsTextureRegion = null;
    public ITextureRegion backgroundStars2TextureRegion = null;
    public ITextureRegion backgroundStars3TextureRegion = null;
    public ITextureRegion backgroundNovaTextureRegion = null;
    public ITextureRegion asteroidTexture = null;
    public ITiledTextureRegion playerTexture = null;
    public ITextureRegion earthTexture = null;
    public ITextureRegion saturnTexture = null;
    public ITextureRegion healthTexture = null;
    public ITextureRegion alienTexture = null;
    public ITextureRegion blackHoleTexture = null;
    public ITextureRegion controlPanel = null;
    public ITextureRegion fireExplosionTexture = null;

    // Gameover Scene Atlas & Texture
    public BuildableBitmapTextureAtlas gameoverTextureAtlas = null;
    public ITextureRegion gameoverBackgroundTexture = null;
    public ITextureRegion gameoverRetryButtonTexture = null;
    public ITextureRegion gameoverSubmitButtonTexture = null;
    public ITextureRegion gameoverLeaderButtonTexture = null;

    // Splash Scene Atlas & Texture
    private BitmapTextureAtlas splashTextureAtlas = null;
    public ITextureRegion logo_texture = null;

    // Intro Scene Atlas & Texture
    private BuildableBitmapTextureAtlas introTextureAtlas = null;
    public ITextureRegion introBackgroundTexture = null;
    public ITextureRegion introPlayButtonTexture = null;
    public ITextureRegion introQuitButtonTexture = null;

    /**
     * Constructor for setting up all instance variables for the game
     * 
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     */
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom) {
	getInstance().engine = engine;
	getInstance().activity = activity;
	getInstance().camera = camera;
	getInstance().vbom = vbom;
    }

    /**
     * Returns the singleton instance of the resource manager
     * 
     * @return
     */
    public static ResourcesManager getInstance() {
	return INSTANCE;
    }

    /**
     * Loads all game resources
     */
    public void loadGameResources() {
	loadGameGraphics();
    }

    /**
     * Loads all gameover resources
     */
    public void loadGameoverResources() {
	loadGameoverGraphics();
    }

    /**
     * Loads game scene graphics
     */
    private void loadGameGraphics() {
	FontFactory.setAssetBasePath("font/");
	final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	gameScoreFont = FontFactory.createFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Plok.ttf", (float) 50, true, Color.BLUE.getABGRPackedInt());
	gameScoreFont.load();

	final ITexture gameoverFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	gameoverScoreFont = FontFactory.createFromAsset(activity.getFontManager(), gameoverFontTexture, activity.getAssets(), "Plok.ttf", (float) 50, true, Color.BLUE.getABGRPackedInt());
	gameoverScoreFont.load();

	MusicFactory.setAssetBasePath("mfx/");
	try {
	    gameplayMusic = MusicFactory.createMusicFromAsset(this.engine.getMusicManager(), activity, "background_music_aac.wav");
	} catch (IllegalStateException e1) {
	    e1.printStackTrace();
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
	gameplayMusic.setLooping(true);

	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);

	backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "background_v2.png");
	backgroundStarsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "stars_v2_1.png");
	backgroundStars2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "stars_v2_2.png");
	backgroundStars3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "stars_v2_3.png");
	backgroundNovaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "nova_v.png");
	asteroidTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "meteor_v2.png");
	playerTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "ship_v3.png", 2, 1);
	earthTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "planet_v2_blue.png");
	saturnTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "planet_v2_red.png");
	healthTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "healthBar.png");
	blackHoleTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "blackHole.png");
	controlPanel = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controlPanel.png");
	alienTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "alien.png");
	fireExplosionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "particle_fire.png");

	gamePhysicsWorld = new GamePhysicsWorld();

	try {
	    this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.gameTextureAtlas.load();
	} catch (final TextureAtlasBuilderException e) {
	    Debug.e(e);
	}
    }

    /**
     * Loads gameover screen graphics
     */
    public void loadGameoverGraphics() {
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	gameoverTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);

	gameoverBackgroundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameoverTextureAtlas, activity, "gameover.png");
	gameoverRetryButtonTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameoverTextureAtlas, activity, "gameoverRetry.png");
	gameoverSubmitButtonTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameoverTextureAtlas, activity, "gameoverSubmit.png");
	gameoverLeaderButtonTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameoverTextureAtlas, activity, "gameoverLeader.png");

	try {
	    this.gameoverTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.gameoverTextureAtlas.load();
	} catch (final TextureAtlasBuilderException e) {
	    Debug.e(e);
	}
    }

    /**
     * Loads splash screen graphics
     */
    public void loadSplashScreen() {
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
	logo_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "ironbrand_logo.png", 0, 0);
	splashTextureAtlas.load();
    }

    /**
     * Unloads splash scene graphics
     */
    public void unloadSplashScreen() {
	splashTextureAtlas.unload();
	logo_texture = null;
    }

    /**
     * Loads intro screen graphics
     */
    public void loadIntroScreen() {
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	introTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
	introBackgroundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(introTextureAtlas, activity, "intro.png");
	introPlayButtonTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(introTextureAtlas, activity, "introPlay.png");
	introQuitButtonTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(introTextureAtlas, activity, "introQuit.png");

	try {
	    this.introTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.introTextureAtlas.load();
	} catch (final TextureAtlasBuilderException e) {
	    Debug.e(e);
	}

    }

    /**
     * Unloads intro scene graphics
     */
    public void unloadIntroScreen() {
	introTextureAtlas.unload();
	introBackgroundTexture = null;
	introPlayButtonTexture = null;
	introQuitButtonTexture = null;
    }

    /**
     * Unloads game scene graphics
     */
    public void unloadGameTextures() {
	this.gameTextureAtlas.unload();
	this.gameScoreFont.unload();
	this.backgroundTextureRegion = null;
	this.backgroundStarsTextureRegion = null;
	this.backgroundStars2TextureRegion = null;
	this.backgroundStars3TextureRegion = null;
	this.backgroundNovaTextureRegion = null;
	this.asteroidTexture = null;
	this.playerTexture = null;
	this.healthTexture = null;
	this.blackHoleTexture = null;
	this.saturnTexture = null;
	this.earthTexture = null;
	this.alienTexture = null;
	this.fireExplosionTexture = null;
    }

    /**
     * Unloads gameover scene graphics
     */
    public void unloadGameoverTextures() {
	this.gameoverTextureAtlas.unload();
	this.gameoverScoreFont.unload();
	this.gameoverRetryButtonTexture = null;
	this.gameoverSubmitButtonTexture = null;
	this.gameoverLeaderButtonTexture = null;
	this.gameoverBackgroundTexture = null;
    }
}
