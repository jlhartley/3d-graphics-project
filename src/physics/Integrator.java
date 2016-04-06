package physics;

import java.util.List;

import entities.celestial.Planet;
import entities.celestial.Star;
import math.geometry.Vector3f;

public class Integrator {
	
	/*
	private static class PlanetDerivates {
		Vector3f position;
		Vector3f velocity;
		Vector3f acceleration;
		Vector3f jerk;
		Vector3f jounce;
	}
	*/
	
	private List<Planet> planets;
	private Star sun;
	
	public Integrator(List<Planet> planets, Star sun) {
		this.planets = planets;
		this.sun = sun;
	}
	
	
	public void symplecticEuler(float deltaTime) {
		
		for (int i = 0; i < planets.size(); i++) {
			
			Planet planet1 = planets.get(i);
			Vector3f acceleration1 = planet1.getAcceleration();
			
			for (int j = i + 1; j < planets.size(); j++) {
				
				Planet planet2 = planets.get(j);
				Vector3f acceleration2 = planet2.getAcceleration();
				
				Vector3f force = planet1.forceTo(planet2);
				
				acceleration1.add(force);
				acceleration2.add(force.negate());
			}
			
			// Add the vector for the sun
			acceleration1.add(planet1.forceTo(sun));
			
			// Divide force vector by mass to give acceleration vector
			float mass = planet1.getMass();
			acceleration1.scale(1 / mass);
			
			Vector3f velocity = planet1.getVelocity();
			velocity.x += acceleration1.x * deltaTime;
			velocity.y += acceleration1.y * deltaTime;
			velocity.z += acceleration1.z * deltaTime;
			
			// Clear ready for next frame computation
			acceleration1.zero();
		}
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			
			Vector3f position = planet.getPosition();
			Vector3f velocity = planet.getVelocity();
			
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
			position.z += velocity.z * deltaTime;
			
			// Rotate planet with time - a full rotation in 2 seconds
			planet.rotateY(180, deltaTime);
		}
		
	}
	
	public void symplecticEuler2(float deltaTime) {
		
		for (int i = 0; i < planets.size(); i++) {
			
			Planet planet1 = planets.get(i);
			Vector3f acceleration1 = planet1.getAcceleration();
			
			for (int j = i + 1; j < planets.size(); j++) {
				
				Planet planet2 = planets.get(j);
				Vector3f acceleration2 = planet2.getAcceleration();
				
				Vector3f force = planet1.forceTo(planet2);
				
				acceleration1.add(force);
				acceleration2.add(force.negate());
			}
			
			// Add the vector for the sun
			acceleration1.add(planet1.forceTo(sun));
			
			// Divide force vector by mass to give acceleration vector
			float mass = planet1.getMass();
			acceleration1.scale(1 / mass);
			
			Vector3f velocity = planet1.getVelocity();
			velocity.x = velocity.x + acceleration1.x * deltaTime;
			velocity.y = velocity.y + acceleration1.y * deltaTime;
			velocity.z = velocity.z + acceleration1.z * deltaTime;
			
		}
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			
			Vector3f position = planet.getPosition();
			Vector3f velocity = planet.getVelocity();
			Vector3f acceleration = planet.getAcceleration();
			
			position.x = position.x + velocity.x * deltaTime - (0.5f * acceleration.x * deltaTime * deltaTime);
			// Neat effect
			//position.y = position.y + velocity.x * deltaTime - (0.5f * acceleration.y * deltaTime * deltaTime);
			position.y = position.y + velocity.y * deltaTime - (0.5f * acceleration.y * deltaTime * deltaTime);
			position.z = position.z + velocity.z * deltaTime - (0.5f * acceleration.z * deltaTime * deltaTime);
			
			// Rotate planet with time - a full rotation in 2 seconds
			planet.rotateY(180, deltaTime);
			
			// Clear ready for next frame computation
			acceleration.zero();
		}
		
	}
	

	
	
	
	
	/*
	
	private float lastFrameDelta = 0;
	
	public void verlet(float deltaTime) {
		
		if (deltaTime == 0) {
			return;
		}
		
		if (lastFrameDelta == 0) {
			lastFrameDelta = deltaTime;
		}
		
		for (int i = 0; i < planets.size(); i++) {
			
			Planet planet1 = planets.get(i);
			Vector3f acceleration1 = planet1.getAcceleration();
			
			for (int j = i + 1; j < planets.size(); j++) {
				
				Planet planet2 = planets.get(j);
				Vector3f acceleration2 = planet2.getAcceleration();
				
				Vector3f force = planet1.forceTo(planet2);
				
				acceleration1.add(force);
				acceleration2.add(force.negate());
			}
			
			// Add the vector for the sun
			acceleration1.add(planet1.forceTo(sun));
			
			// Divide force vector by mass to give acceleration vector
			float mass = planet1.getMass();
			acceleration1.scale(1 / mass);
		}
		
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			
			Vector3f position = planet.getPosition();
			//Vector3f velocity = planet.getVelocity();
			Vector3f acceleration = planet.getAcceleration();
			
			Vector3f oldPosition = planetFrameDataMap.get(planet).oldPosition;
			
			float newXPos = position.x + (position.x - oldPosition.x) * (deltaTime / lastFrameDelta) + acceleration.x * deltaTime * deltaTime;
			float newYPos = position.y + (position.y - oldPosition.y) * (deltaTime / lastFrameDelta) + acceleration.y * deltaTime * deltaTime;
			float newZPos = position.z + (position.z - oldPosition.z) * (deltaTime / lastFrameDelta) + acceleration.z * deltaTime * deltaTime;
			
			oldPosition.set(position);
			position.set(newXPos, newYPos, newZPos);
			
			// Rotate planet with time - a full rotation in 2 seconds
			planet.rotateY(180, deltaTime);
			
			// Clear ready for next frame computation
			acceleration.zero();
		}
		
		lastFrameDelta = deltaTime;
		
	}
	
	*/
}
