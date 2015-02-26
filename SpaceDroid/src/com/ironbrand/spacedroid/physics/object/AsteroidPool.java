package com.ironbrand.spacedroid.physics.object;

import org.andengine.util.adt.pool.GenericPool;

/**
 * Class container for asteroid pool.
 * 
 * @author bwinters
 * 
 */
public class AsteroidPool extends GenericPool<Asteroid> {

    private static final String TAG = AsteroidPool.class.getName();

    /**
     * Called when a asteroid is required but there isn't one in the pool
     */
    @Override
    protected Asteroid onAllocatePoolItem() {
//	Log.d(TAG, "******ASTEROID - Creating New Object******");
	Asteroid asteroid = new Asteroid();
	return asteroid;
    }

    /**
     * Retrieve item from the pool
     */
    @Override
    protected void onHandleObtainItem(final Asteroid pFace) {
	super.onHandleObtainItem(pFace);
	pFace.reset();
    }

    /**
     * Recycle item into the pool
     */
    @Override
    protected void onHandleRecycleItem(Asteroid asteroid) {
//	Log.d(TAG, "******ASTEROID - Recycling Object******");
	super.onHandleRecycleItem(asteroid);
	asteroid.clearEntityModifiers();
	asteroid.clearUpdateHandlers();
	asteroid.setVisible(false);
	asteroid.setIgnoreUpdate(true);
	asteroid.detachSelf();
	asteroid.reset();
    }
}
