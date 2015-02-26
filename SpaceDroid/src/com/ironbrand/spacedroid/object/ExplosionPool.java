package com.ironbrand.spacedroid.object;

import org.andengine.util.adt.pool.GenericPool;

/**
 * Class container for alien pool.
 * 
 * @author bwinters
 * 
 */
public class ExplosionPool extends GenericPool<Explosion> {

    private static final String TAG = ExplosionPool.class.getName();

    /**
     * Called when a projectile is required but there isn't one in the pool
     */
    @Override
    protected Explosion onAllocatePoolItem() {
//	Log.d(TAG, "******EXPLOSION - Creating New Object******");
	Explosion explosion = new Explosion();
	return explosion;
    }

    /**
     * Retrieve item from pool
     */
    @Override
    protected void onHandleObtainItem(final Explosion pFace) {
	super.onHandleObtainItem(pFace);
	pFace.reset();
    }

    /**
     * Recycle item into pool
     */
    @Override
    protected void onHandleRecycleItem(Explosion explosion) {
//	Log.d(TAG, "******EXPLOSION - Recycling Object******");
	super.onHandleRecycleItem(explosion);
	explosion.setParticlesSpawnEnabled(false);
	explosion.clearEntityModifiers();
	explosion.clearUpdateHandlers();
	explosion.setVisible(false);
	explosion.setIgnoreUpdate(true);
	explosion.detachSelf();
	explosion.reset();
    }
}
