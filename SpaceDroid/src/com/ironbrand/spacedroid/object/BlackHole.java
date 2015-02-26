/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.modifier.ease.EaseSineInOut;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * BlackHole Class
 * 
 * @author bwinters
 * 
 */
public class BlackHole extends Sprite {
    private static final float X_COORD = 745;
    private static final float Y_COORD = 105;
    private static final float WIDTH = 65;
    private static final float HEIGHT = 65;
    private static final float SCALE_FACTOR = 1.0f;
    private final Path path = new Path(3).to(725, 105).to(725, 410).to(725, 105);

    /**
     * BlackHole Constructor
     */
    public BlackHole() {
	super(X_COORD, Y_COORD, WIDTH, HEIGHT, ResourcesManager.getInstance().blackHoleTexture, ResourcesManager.getInstance().vbom);
	this.setScale(BlackHole.SCALE_FACTOR);
	this.registerEntityModifier(new LoopEntityModifier(new ParallelEntityModifier(new RotationModifier(3.0f, 0.0f, 360), new ScaleModifier(1.5f, 0.0f, 1.0f))));
	createPath();
    }

    /**
     * Creates the path that the black hole will follow up/down on right side of
     * screen.
     */
    private void createPath() {
	/* Add the proper animation when a waypoint of the path is passed. */
	this.registerEntityModifier(new LoopEntityModifier(new PathModifier(4, path, null, new IPathModifierListener() {
	    @Override
	    public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
		// Debug.d("onPathStarted");
	    }

	    @Override
	    public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
		// Debug.d("onPathWaypointStarted:  " + pWaypointIndex);
	    }

	    @Override
	    public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
		// Debug.d("onPathWaypointFinished: " + pWaypointIndex);
	    }

	    @Override
	    public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
		// Debug.d("onPathFinished");
	    }
	}, EaseSineInOut.getInstance())));
    }
}
