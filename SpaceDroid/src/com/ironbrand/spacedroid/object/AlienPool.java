package com.ironbrand.spacedroid.object;

import org.andengine.util.adt.pool.GenericPool;

/**
 * Class container for alien pool.
 * 
 * @author bwinters
 * 
 */
public class AlienPool extends GenericPool<Alien> {

    private static final String TAG = AlienPool.class.getName();

    /**
     * Called when a projectile is required but there isn't one in the pool
     */
    @Override
    protected Alien onAllocatePoolItem() {
//	Log.d(TAG, "******ALIEN - Creating New Object******");
	Alien alien = new Alien();
	return alien;
    }

    /**
     * Retrieve item from pool
     */
    @Override
    protected void onHandleObtainItem(final Alien pFace) {
	super.onHandleObtainItem(pFace);
	pFace.reset();
    }

    /**
     * Recycle item into pool
     */
    @Override
    protected void onHandleRecycleItem(Alien alien) {
//	Log.d(TAG, "******ALIEN - Recycling Object******");
	super.onHandleRecycleItem(alien);
	alien.clearEntityModifiers();
	alien.clearUpdateHandlers();
	alien.setVisible(false);
	alien.setIgnoreUpdate(true);
	alien.detachSelf();
	alien.reset();
    }
}
