package com.ironbrand.spacedroid.scene;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.ironbrand.spacedroid.base.BaseScene;
import com.ironbrand.spacedroid.manager.ResourcesManager;
import com.ironbrand.spacedroid.manager.SceneManager;
import com.ironbrand.spacedroid.manager.SceneManager.SceneType;
import com.ironbrand.spacedroid.object.Alien;
import com.ironbrand.spacedroid.object.AlienPool;
import com.ironbrand.spacedroid.object.BlackHole;
import com.ironbrand.spacedroid.object.ControlPanel;
import com.ironbrand.spacedroid.object.Explosion;
import com.ironbrand.spacedroid.object.ExplosionPool;
import com.ironbrand.spacedroid.object.GameScore;
import com.ironbrand.spacedroid.object.GameScoreDifference;
import com.ironbrand.spacedroid.object.GameoverScore;
import com.ironbrand.spacedroid.object.HealthBar;
import com.ironbrand.spacedroid.object.HealthBarBackground;
import com.ironbrand.spacedroid.object.Planet;
import com.ironbrand.spacedroid.object.PlanetPool;
import com.ironbrand.spacedroid.physics.object.Asteroid;
import com.ironbrand.spacedroid.physics.object.AsteroidPool;
import com.ironbrand.spacedroid.physics.object.BottomWall;
import com.ironbrand.spacedroid.physics.object.LeftWall;
import com.ironbrand.spacedroid.physics.object.RightWall;
import com.ironbrand.spacedroid.physics.object.Spaceship;
import com.ironbrand.spacedroid.physics.object.TopWall;

/**
 * Main Game Scene class responsible for game controls and logic
 * 
 * @author bwinters
 * 
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private static final String TAG = GameScene.class.getName();
    public static int MAX_HEALTH = 200;
    private static float HEALTH_DECREMENT = MAX_HEALTH * 0.05f;
    private static float HEALTH_CRITICAL_THRESHOLD = 2 * HEALTH_DECREMENT;

    private static float ASTEROID_VELOCITY = 0.05f;
    private static float ASTEROID_GEN_DELAY = 2.0f;
    private static float PLANET_GEN_DELAY = 1.0f;
    private static float EXPLOSION_EXPIRE_DELAY = 1f;
    private static float ALIEN_GEN_DELAY = 1.0f;

    private float currentHealth = 200; // This value is updated
    private GameScore gameScore;

    private GameScoreDifference gameScoreDifference;
    private HUD gameHud = null;

    /* Sprite pools used for recycling */
    private AsteroidPool asteroidPool = new AsteroidPool();
    private LinkedList<Asteroid> asteroidList = new LinkedList<Asteroid>();
    private LinkedList<Asteroid> recycledAsteroidList = new LinkedList<Asteroid>();
    private AlienPool alienPool = new AlienPool();
    private LinkedList<Alien> alienList = new LinkedList<Alien>();
    private LinkedList<Alien> recycledAlienList = new LinkedList<Alien>();
    private PlanetPool planetPool = new PlanetPool();
    private LinkedList<Planet> planetList = new LinkedList<Planet>();
    private LinkedList<Planet> recycledPlanetList = new LinkedList<Planet>();

    private ExplosionPool explosionPool = new ExplosionPool();
    private LinkedList<Explosion> explosionList = new LinkedList<Explosion>();
    private LinkedList<Explosion> recycledExplosionList = new LinkedList<Explosion>();

    /* Sprite characters in the game */
    private Spaceship spaceship;
    private TopWall topWall;
    private LeftWall leftWall;
    private RightWall rightWall;
    private BottomWall bottomWall;
    private HealthBar healthBar;
    private HealthBarBackground healthBarBackground;
    private ControlPanel controlPanel;
    private BlackHole blackHole;

    /* In-Game Timers */
    private TimerHandler asteroidTimerHandler;
    private TimerHandler alienTimerHandler;
    private TimerHandler planetTimerHandler;
    private TimerHandler explosionTimerHandler;
    private IUpdateHandler gameSceneHandler;

    private boolean isGameOver = false;

    /**
     * Creates the game scene
     */
    @Override
    public void createScene() {
	createBackground();
	createBoundaries();
	createHUD();
	createPhysics();
	createEnemyHandlers();
	createGameSceneHandler();
	createBlackHole();
	createPlayer();

	this.setOnSceneTouchListener(this);
	this.sortChildren();
    }

    /**
     * Create game scene boundaries
     */
    private void createBoundaries() {
	this.topWall = new TopWall();
	this.attachChild(this.topWall);

	this.leftWall = new LeftWall();
	this.attachChild(this.leftWall);

	this.rightWall = new RightWall();
	this.attachChild(this.rightWall);

	this.bottomWall = new BottomWall();
	this.attachChild(this.bottomWall);
    }

    /**
     * Creates and attaches spaceship to the scene
     */
    private void createPlayer() {
	spaceship = new Spaceship();
	this.attachChild(spaceship);
    }

    /**
     * Adds the black hole to game scene
     */
    private void createBlackHole() {
	blackHole = new BlackHole();
	this.attachChild(blackHole);
    }

    /**
     * Registers the physics world on scene.
     */
    private void createPhysics() {
	registerUpdateHandler(resourcesManager.gamePhysicsWorld);
	resourcesManager.engine.enableVibrator(resourcesManager.activity);
    }

    /**
     * Handles onBackKeyPressed()
     * 
     * @see com.ironbrand.spacedroid.base.BaseScene#onBackKeyPressed()
     */
    @Override
    public void onBackKeyPressed() {
	SceneManager.getInstance().exitGame();
    }

    /**
     * Returns the game scene enum
     */
    @Override
    public SceneType getSceneType() {
	return SceneType.SCENE_GAME;
    }

    /**
     * Dipsoses all games scene entities and objects
     */
    @Override
    public void disposeScene() {
	engine.runOnUpdateThread(new Runnable() {
	    @Override
	    public void run() {
		clearTouchAreas();
		clearUpdateHandlers();
		clearSceneEntities();
		asteroidList.clear();
		asteroidList = null;
		alienList.clear();
		alienList = null;
		planetList.clear();
		planetList = null;
		explosionList.clear();
		explosionList = null;
		recycledAlienList.clear();
		recycledAlienList = null;
		recycledPlanetList.clear();
		recycledPlanetList = null;
		recycledAsteroidList.clear();
		recycledAsteroidList = null;
		recycledExplosionList.clear();
		recycledExplosionList = null;
		resourcesManager.gamePhysicsWorld.clearPhysicsWorld();
		resourcesManager.gamePhysicsWorld = null;
		gameScoreDifference = null;
		healthBar.setVisible(false);
		healthBar.detachSelf();
		healthBar = null;
		healthBarBackground = null;
		controlPanel = null;
		GameScore.getGameScore().detachSelf();
		GameScore.getGameScore().dispose();
		GameScore.setGameScore(null);
		System.gc();
	    }
	});
    }

    /**
     * Creates the control panel and health bar graphics
     */
    private void createHUD() {
	gameHud = new HUD();

	this.controlPanel = new ControlPanel();
	gameHud.attachChild(this.controlPanel);

	this.healthBarBackground = new HealthBarBackground();
	gameHud.attachChild(this.healthBarBackground);

	this.healthBar = new HealthBar();
	gameHud.attachChild(this.healthBar);

	/* Add scoring text */
	this.gameScore = GameScore.getGameScore();
	gameHud.attachChild(this.gameScore);

	/* Add scoring add text */
	this.gameScoreDifference = new GameScoreDifference();
	gameHud.attachChild(this.gameScoreDifference);

	camera.setHUD(gameHud);
	camera.setBounds(0, 0, camera.getWidth(), camera.getHeight());
	camera.setBoundsEnabled(true);
    }

    /**
     * Creates the moving background
     */
    private void createBackground() {
	final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 25);
	setBackground(autoParallaxBackground);

	final Sprite parallaxLayerBackSprite = new Sprite(0, 0, resourcesManager.backgroundTextureRegion, resourcesManager.vbom);
	parallaxLayerBackSprite.setOffsetCenter(0, 0);
	autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerBackSprite));

	final Sprite parallaxLayerBackStarsSprite = new Sprite(0, 40, resourcesManager.backgroundStarsTextureRegion, resourcesManager.vbom);
	parallaxLayerBackStarsSprite.setOffsetCenter(0, 0);
	autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-3.0f, parallaxLayerBackStarsSprite));

	final Sprite parallaxLayerBackStars2Sprite = new Sprite(0, 40, resourcesManager.backgroundStars2TextureRegion, resourcesManager.vbom);
	parallaxLayerBackStars2Sprite.setOffsetCenter(0, 0);
	autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.0f, parallaxLayerBackStars2Sprite));

	final Sprite parallaxLayerBackNovaSprite = new Sprite(0, 40, resourcesManager.backgroundNovaTextureRegion, resourcesManager.vbom);
	parallaxLayerBackNovaSprite.setOffsetCenter(0, 0);
	autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-1.0f, parallaxLayerBackNovaSprite));
    }

    /**
     * Handles touch events for the scene
     */
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	if (pSceneTouchEvent.isActionDown()) {
	    spaceship.floatUp(pSceneTouchEvent);
	} else if (pSceneTouchEvent.isActionMove()) {
	} else if (pSceneTouchEvent.isActionUp()) {
	}
	return true;
    }

    /**
     * Create game timers for asteroids/aliens/planets Initialize pools for each
     * entity
     */
    public void createEnemyHandlers() {
	asteroidTimerHandler = new TimerHandler(ASTEROID_GEN_DELAY, true, new ITimerCallback() {

	    @Override
	    public void onTimePassed(TimerHandler pTimerHandler) {
		if (isGameOver() == false) {
		    addAsteroidToScene();
		}
	    }
	});
	engine.registerUpdateHandler(asteroidTimerHandler);

	planetTimerHandler = new TimerHandler(PLANET_GEN_DELAY, true, new ITimerCallback() {

	    @Override
	    public void onTimePassed(TimerHandler pTimerHandler) {
		if (isGameOver() == false) {
		    addTraversingPlanet();
		    spaceship.clearEntityModifiers();
		    spaceship.reset();
		    gameScoreDifference.fade();
		}
	    }
	});
	engine.registerUpdateHandler(planetTimerHandler);

	alienTimerHandler = new TimerHandler(ALIEN_GEN_DELAY, true, new ITimerCallback() {

	    @Override
	    public void onTimePassed(TimerHandler pTimerHandler) {
		if (isGameOver() == false) {
		    addTraverseAlienToScene();
		}
	    }
	});
	engine.registerUpdateHandler(alienTimerHandler);

	explosionTimerHandler = new TimerHandler(EXPLOSION_EXPIRE_DELAY, true, new ITimerCallback() {

	    @Override
	    public void onTimePassed(TimerHandler pTimerHandler) {
		if (isGameOver() == false) {
		    Iterator<Explosion> explosions = explosionList.iterator();
		    Explosion explosion = null;

		    while (explosions.hasNext()) {
			explosion = explosions.next();
			explosionPool.recyclePoolItem(explosion);
			explosions.remove();
		    }

		    explosionList.addAll(recycledExplosionList);
		    recycledExplosionList.clear();
		}
	    }
	});
	engine.registerUpdateHandler(explosionTimerHandler);
    }

    /**
     * Creates main game scene update handler
     */
    private void createGameSceneHandler() {
	gameSceneHandler = new IUpdateHandler() {
	    @Override
	    public void reset() {
	    }

	    @Override
	    public void onUpdate(float pSecondsElapsed) {
		if (isGameOver()) {
		    updateHealthMeter();
		    showGameOver();
		}

		Iterator<Asteroid> asteroids = asteroidList.iterator();
		Iterator<Alien> aliens = alienList.iterator();
		Iterator<Planet> planets = planetList.iterator();

		Asteroid asteroid = null;
		Alien alien = null;
		Planet planet = null;

		while (asteroids.hasNext()) {
		    asteroid = asteroids.next();
		    if (asteroid.getX() <= 0 || asteroid.getY() <= 0 || asteroid.getY() >= camera.getHeight() || asteroid.getX() >= camera.getWidth()) {
			asteroidPool.recyclePoolItem(asteroid);
			asteroids.remove();
			continue;
		    }
		    if (asteroid.collidesWith(spaceship)) {
			spaceship.onHit();
			resourcesManager.engine.vibrate(300);
			currentHealth = (currentHealth - HEALTH_DECREMENT);
		    }
		}

		while (aliens.hasNext()) {
		    alien = aliens.next();
		    if (alien.getY() <= -alien.getHeight() || alien.getX() <= 0) {
			alienPool.recyclePoolItem(alien);
			aliens.remove();
			continue;
		    }

		    if (alien.collidesWith(spaceship)) {
			// Log.d(TAG, "PLAYER COLLIDING WITH ALIEN");
			spaceship.onHit();
			resourcesManager.engine.vibrate(200);
			currentHealth = (currentHealth - HEALTH_DECREMENT);
			addExplosionToScene(alien.getX(), alien.getY());
			alienPool.recyclePoolItem(alien);
			aliens.remove();
			continue;
		    }
		}

		while (planets.hasNext()) {
		    planet = planets.next();
		    if (planet.getX() <= 0 || planet.getY() >= camera.getHeight()) {
			planetPool.recyclePoolItem(planet);
			planets.remove();
			continue;
		    }

		    if (planet.collidesWith(spaceship)) {
			if (planet.getDescription().equals("saturn")) {
			    // Log.d(TAG, "PLAYER COLLIDING WITH SATURN");
			} else if (planet.getDescription().equals("earth")) {
			    // Log.d(TAG, "PLAYER COLLIDING WITH EARTH");
			}
			gameScore.addToScore();
			gameScoreDifference.showScoreDifference(planet.getX(), planet.getY());
			planetPool.recyclePoolItem(planet);
			planets.remove();
			continue;
		    }
		}

		alienList.addAll(recycledAlienList);
		asteroidList.addAll(recycledAsteroidList);
		planetList.addAll(recycledPlanetList);

		recycledAlienList.clear();
		recycledAsteroidList.clear();
		recycledPlanetList.clear();

		updateHealthMeter();
	    }
	};
	this.registerUpdateHandler(gameSceneHandler);
    }

    /**
     * Updates the health meter of the spaceship
     */
    private void updateHealthMeter() {
	if (currentHealth >= MAX_HEALTH) {
	    healthBar.setWidth(MAX_HEALTH);
	    currentHealth = MAX_HEALTH;
	} else {
	    healthBar.setWidth(currentHealth);
	}

	if (currentHealth <= 0.0f && isGameOver() == false) {
	    spaceship.onDie();
	    isGameOver = true;
	} else if (currentHealth <= HEALTH_CRITICAL_THRESHOLD) {
	    healthBar.setColor(Color.RED);
	} else if (currentHealth <= 0.0f && isGameOver() == true) {
	    healthBar.setVisible(false);
	}
    }

    /**
     * Adds asteroids to scene
     */
    private void addAsteroidToScene() {
	Asteroid recycledAsteroid;
	recycledAsteroid = asteroidPool.obtainPoolItem();

	float holeX = blackHole.getX();
	float holeY = blackHole.getY();
	float playerX = spaceship.getX();
	float playerY = spaceship.getY();

	float rise = playerY - holeY;
	float run = playerX - holeX;

	float slope = rise / run;
	float angle = (float) Math.atan(slope);

	// double degrees = Math.toDegrees(angle);

	// Log.d(TAG, "SLOPE [" + slope + "]");
	// Log.d(TAG, "ANGLE [" + angle + "]");
	// Log.d(TAG, "DEGREES [" + degrees + "]");

	Vector2 vel = Vector2Pool.obtain(new Vector2((float) (-300 * (Math.cos(angle))), (float) (-300 * (Math.sin(angle)))));
	recycledAsteroid.getBody().setTransform(new Vector2(holeX / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT, holeY / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT), 0);
	recycledAsteroid.getBody().setLinearVelocity(vel.x * ASTEROID_VELOCITY, vel.y * ASTEROID_VELOCITY);

	recycledAsteroid.registerEntityModifier(new LoopEntityModifier(new RotationModifier(2.0f, 360.0f, 0.0f)));
	recycledAsteroid.getBody().setBullet(true);
	this.attachChild(recycledAsteroid);

	recycledAsteroidList.add(recycledAsteroid);
    }

    /**
     * Adds traversiong planet to scene.
     */
    private void addTraversingPlanet() {
	Planet recycledPlanet = planetPool.obtainPoolItem();
	this.attachChild(recycledPlanet);
	recycledPlanet.traverse();
	recycledPlanetList.add(recycledPlanet);
    }

    /**
     * Adds traversing alien to scene.
     */
    private void addTraverseAlienToScene() {
	Alien recycledAlien = alienPool.obtainPoolItem();
	this.attachChild(recycledAlien);
	recycledAlien.traverse();
	recycledAlienList.add(recycledAlien);
    }

    /**
     * Adds explosion to scene.
     */
    private void addExplosionToScene(float x, float y) {
	final Explosion recycledExplosion = explosionPool.obtainPoolItem();
	((PointParticleEmitter) recycledExplosion.getParticleEmitter()).setCenter(x, y);
	recycledExplosion.setPosition(x, y);
	recycledExplosion.setParticlesSpawnEnabled(true);
	this.attachChild(recycledExplosion);

	recycledExplosionList.add(recycledExplosion);
    }

    /**
     * Shows the gameover window
     */
    private void showGameOver() {
	GameoverScore.getGameoverScore().setScore(GameScore.getGameScore().getScore());
	SceneManager.getInstance().createGameoverScene(ResourcesManager.getInstance().engine);
    }

    /**
     * @return the isGameOver
     */
    public boolean isGameOver() {
	return isGameOver;
    }

    /**
     * Remove all entities from scene.
     */
    private void clearSceneEntities() {
	for (int i = 0; i < this.getChildCount(); i++) {
	    IEntity entity = this.getChildByIndex(i);
	    entity.clearEntityModifiers();
	    entity.clearUpdateHandlers();
	    entity.detachSelf();

	    if (!entity.isDisposed()) {
		entity.dispose();
		entity = null;
	    }
	}

	for (int i = 0; i < camera.getHUD().getChildCount(); i++) {
	    IEntity entity = camera.getHUD().getChildByIndex(i);
	    entity.clearEntityModifiers();
	    entity.clearUpdateHandlers();
	    entity.detachSelf();

	    if (!entity.isDisposed()) {
		entity.dispose();
		entity = null;
	    }
	}

	camera.getHUD().detachSelf();
	camera.getHUD().dispose();
	camera.setHUD(null);
    }
}