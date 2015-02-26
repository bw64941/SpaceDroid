package com.ironbrand.spacedroid.object;

import org.andengine.util.adt.pool.GenericPool;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * 
 * 
 * @author bwinters
 * 
 */
public class PlanetPool extends GenericPool<Planet> {

    private static final String TAG = PlanetPool.class.getName();
    private int counter = 0;

    /**
     * Called when a planet is required but there isn't one in the pool
     */
    @Override
    protected Planet onAllocatePoolItem() {
//	Log.d(TAG, "******PLANET - Creating New Object******");
	Planet planet;
	if (counter % 2 == 0) {
	    planet = new Planet(ResourcesManager.getInstance().saturnTexture);
	    planet.setDescription("saturn");
	} else {
	    planet = new Planet(ResourcesManager.getInstance().earthTexture);
	    planet.setDescription("earth");
	}
	counter++;

	return planet;
    }

    /**
     * Retrieve Item from pool
     */
    @Override
    protected void onHandleObtainItem(final Planet pFace) {
	super.onHandleObtainItem(pFace);
	pFace.reset();
    }

    /**
     * Recycle Item into pool
     */
    @Override
    protected void onHandleRecycleItem(Planet planet) {
//	Log.d(TAG, "******PLANET - Recycling Object******");
	super.onHandleRecycleItem(planet);
	planet.clearEntityModifiers();
	planet.clearUpdateHandlers();
	planet.setVisible(false);
	planet.setIgnoreUpdate(true);
	planet.detachSelf();
	planet.reset();
    }
}
