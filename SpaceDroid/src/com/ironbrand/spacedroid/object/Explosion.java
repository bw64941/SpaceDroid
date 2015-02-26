/**
 * 
 */
package com.ironbrand.spacedroid.object;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;

import android.opengl.GLES20;

import com.ironbrand.spacedroid.manager.ResourcesManager;

/**
 * @author bwinters
 * 
 */
public class Explosion extends BatchedPseudoSpriteParticleSystem {

    private static final float RATE_MIN = 160;
    private static final float RATE_MAX = 160;
    private static final int PARTICLES_MAX = 100;

    public Explosion() {
	// Particle emitter which will set all of the particles at a ertain
	// point when they are initialized.
	super(0, 0, new PointParticleEmitter(0, 0), RATE_MIN, RATE_MAX, PARTICLES_MAX, ResourcesManager.getInstance().fireExplosionTexture, ResourcesManager.getInstance().vbom);
	this.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

	// And now, lets create the initiallizers and modifiers.
	// Velocity initiallizer - will pick a random velocity from -20 to 20 on
	// the x & y axes. Play around with this value.
	this.addParticleInitializer(new VelocityParticleInitializer<Entity>(-50, 50, -5, 5));

	// Acceleration initializer - gives all the particles the earth gravity
	// (so they accelerate down).
	this.addParticleInitializer(new GravityParticleInitializer<Entity>());

	// And now, adding an alpha modifier, so particles slowly fade out. This
	// makes a particle go from alpha = 1 to alpha = 0 in 3 seconds,
	// starting exactly when the particle is spawned.
	this.addParticleModifier(new AlphaParticleModifier<Entity>(0, 3, 1, 0));
	this.addParticleModifier(new ColorParticleModifier<Entity>(0, 1, 1, 1, 0, 0.5f, 0, 0));
	this.addParticleModifier(new ColorParticleModifier<Entity>(1, 2, 1, 1, 0.5f, 1, 0, 1));

	// Lastly, expire modifier. Make particles die after 3 seconds - their
	// alpha reached 0.
	this.addParticleInitializer(new ExpireParticleInitializer<Entity>(2));
    }
}
