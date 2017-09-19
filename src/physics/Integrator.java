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
			
		}
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			
			Vector3f position = planet.getPosition();
			Vector3f velocity = planet.getVelocity();
			Vector3f acceleration = planet.getAcceleration();
			
			velocity.x = velocity.x + acceleration.x * deltaTime;
			velocity.y = velocity.y + acceleration.y * deltaTime;
			velocity.z = velocity.z + acceleration.z * deltaTime;
			
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
	
	
	//Vector3f r = new Vector3f();
	
	public void symplecticEuler3(float deltaTime) {
		
		for (int i = 0; i < planets.size(); i++) {
			
			Planet planet1 = planets.get(i);
			float mass1 = planet1.getMass();
			Vector3f position1 = planet1.getPosition();
			Vector3f acceleration1 = planet1.getAcceleration();
			
			for (int j = i + 1; j < planets.size(); j++) {
				
				Planet planet2 = planets.get(j);
				float mass2 = planet2.getMass();
				Vector3f position2 = planet2.getPosition();
				Vector3f acceleration2 = planet2.getAcceleration();
				
				float dx = position2.x - position1.x;
				float dy = position2.y - position1.y;
				float dz = position2.z - position1.z;
				
				
				float inv = (float) (1 / Math.sqrt(dx*dx + dy*dy + dz*dz));
				float inv3 = inv*inv*inv * Constants.G;
				
				// Unscaled acceleration;
				float uax = dx * inv3;
				float uay = dy * inv3;
				float uaz = dz * inv3;
				
				//acceleration1.add(force);
				acceleration1.x += uax * mass2;
				acceleration1.y += uay * mass2;
				acceleration1.z += uaz * mass2;
				
				//acceleration2.add(force.negate());
				acceleration2.x += -uax * mass1;
				acceleration2.y += -uay * mass1;
				acceleration2.z += -uaz * mass1;
			}
			
			// Add the vector for the sun
			acceleration1.add(planet1.forceTo(sun).scale(1 / mass1));
			
			// Divide force vector by mass to give acceleration vector
			//float mass = planet1.getMass();
			//acceleration1.scale(1 / mass);
			
			//acceleration1.scale(Constants.G);
			
		}
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			
			Vector3f position = planet.getPosition();
			Vector3f velocity = planet.getVelocity();
			Vector3f acceleration = planet.getAcceleration();
			
			velocity.x = velocity.x + acceleration.x * deltaTime;
			velocity.y = velocity.y + acceleration.y * deltaTime;
			velocity.z = velocity.z + acceleration.z * deltaTime;
			
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
	
	
	public void symplecticEuler4(float deltaTime) {
		
		for (int i = 0; i < planets.size(); i++) {
			
			Planet planet1 = planets.get(i);
			Vector3f acceleration1 = planet1.getAcceleration();
			
			for (int j = i + 1; j < planets.size(); j++) {
				
				Planet planet2 = planets.get(j);
				Vector3f acceleration2 = planet2.getAcceleration();
				
				Vector3f uac = Vector3f.sub(planet2.getPosition(), planet1.getPosition());
				float mag = uac.magnitude();
				uac.scale(Constants.G / (mag*mag*mag));
				
				acceleration1.x += uac.x * planet2.getMass();
				acceleration1.y += uac.y * planet2.getMass();
				acceleration1.z += uac.z * planet2.getMass();
				
				acceleration2.x -= uac.x * planet1.getMass();
				acceleration2.y -= uac.y * planet1.getMass();
				acceleration2.z -= uac.z * planet1.getMass();
			}
			
			// Add the vector for the sun
			acceleration1.add(planet1.forceTo(sun).scale(1 / planet1.getMass()));
			
			// Divide force vector by mass to give acceleration vector
			//float mass = planet1.getMass();
			//acceleration1.scale(1 / mass);
			
		}
		
		// Wait until all planet force calculations (dependent on position)
		// are done, before updating the position of each planet
		for (Planet planet : planets) {
			
			Vector3f position = planet.getPosition();
			Vector3f velocity = planet.getVelocity();
			Vector3f acceleration = planet.getAcceleration();
			
			velocity.x = velocity.x + acceleration.x * deltaTime;
			velocity.y = velocity.y + acceleration.y * deltaTime;
			velocity.z = velocity.z + acceleration.z * deltaTime;
			
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
	
	
	public void symplecticEuler5(float deltaTime) {
		
		for (Planet p1 : planets) {
			
			Vector3f a1 = p1.getAcceleration();
			
			for (Planet p2 : planets) {
				
				if (p1 == p2) continue;
				
				Vector3f acc = Vector3f.sub(p2.getPosition(), p1.getPosition());
				float mag = acc.magnitude();
				acc.scale(p2.getMass() / (mag*mag*mag));
				
				a1.add(acc);
				
			}
			
			Vector3f acc = Vector3f.sub(sun.getPosition(), p1.getPosition());
			float mag = acc.magnitude();
			acc.scale(sun.getMass() / (mag*mag*mag));
			
			a1.add(acc);
			
			
			
			a1.scale(Constants.G);
			
		}
		
		
		for (Planet p : planets) {
			Vector3f position = p.getPosition();
			Vector3f velocity = p.getVelocity();
			Vector3f acceleration = p.getAcceleration();
			
			velocity.x = velocity.x + acceleration.x * deltaTime;
			velocity.y = velocity.y + acceleration.y * deltaTime;
			velocity.z = velocity.z + acceleration.z * deltaTime;
			
			position.x = position.x + velocity.x * deltaTime - (0.5f * acceleration.x * deltaTime * deltaTime);
			position.y = position.y + velocity.y * deltaTime - (0.5f * acceleration.y * deltaTime * deltaTime);
			position.z = position.z + velocity.z * deltaTime - (0.5f * acceleration.z * deltaTime * deltaTime);
			
			// Rotate planet with time - a full rotation in 2 seconds
			p.rotateY(180, deltaTime);
			
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
