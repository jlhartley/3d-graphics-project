package physics;

import entities.celestial.CelestialEntity;
import math.geometry.Vector3f;

public class PhysicsUtils {
	
	// Bit of a stupid level of abstraction here - not really needed
	
	public static double getVolume(double mass, double density) {
		return mass / density;
	}
	
	public static double getDensity(double mass, double volume) {
		return mass / volume;
	}
	
	public static double getMass(double density, double volume) {
		return density * volume;
	}
	
	
	// Get a stable orbital velocity for a circular orbit of entity1 around entity2.
	// This is assuming entity2 is stationary, and both entities lie in the same plane.
	public static Vector3f getStableOrbitalVelocity(CelestialEntity entity1, CelestialEntity entity2) {
		Vector3f p1 = entity1.getPosition();
		Vector3f p2 = entity2.getPosition();
		float m1 = entity1.getMass();
		float m2 = entity2.getMass();
		Vector3f r = Vector3f.sub(p2, p1);
		
		//Vector3f vel = Vector3f.cross(r, new Vector3f((float)Math.random() - 0.5f, (float)Math.random() - 0.5f, (float)Math.random() - 0.5f));
		Vector3f vel = Vector3f.cross(r, new Vector3f(0, 1, 0));
		
		Vector3f velocity = new Vector3f();
		// Perpendicular vector in the x-z plane
		velocity.x = -r.z;
		velocity.z = r.x;
		// Calculate velocity magnitude (speed)
		double distance = r.magnitude();
		double speed = Math.sqrt(Constants.G  * (m1 + m2) / distance);
		velocity.setMagnitude((float) speed);
		
		vel.setMagnitude((float) speed);
		
		return vel;
	}
	
}
