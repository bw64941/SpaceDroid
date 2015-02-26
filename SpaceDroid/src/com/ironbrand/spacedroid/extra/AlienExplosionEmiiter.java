/**
 * 
 */
package com.ironbrand.spacedroid.extra;

import org.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;

/**
 * @author bwinters
 * 
 */
public class AlienExplosionEmiiter extends CircleOutlineParticleEmitter {

    private static final float CENTER_X = 0.0f;
    private static final float CENTER_Y = 0.0f;
    private static final float RADIUS = 5.0f;

    public AlienExplosionEmiiter() {
	super(AlienExplosionEmiiter.CENTER_X, AlienExplosionEmiiter.CENTER_Y, AlienExplosionEmiiter.RADIUS);
    }

}
