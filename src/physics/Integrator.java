package physics;

public class Integrator {
	
	/*
	
	//ArrayList<Vector3f> newPositions = new ArrayList<>();
	
	for (Planet planet1 : planets) {
		
		Vector3f resultantAcceleration = new Vector3f();
		
		for (Planet planet2 : planets) {
			
			// Avoid comparing a planet to itself
			if (planet1 == planet2) {
				continue;
			}
			
			// Adding vectors gives a resultant vector
			resultantAcceleration.add(planet1.accelerationVectorTo(planet2));
		}
		
		// Add acceleration vector for the sun
		resultantAcceleration.add(planet1.accelerationVectorTo(sun));
		
		
		Vector3f newPosition = new Vector3f(planet1.getPosition());
		Vector3f velocity = planet1.getVelocity();
		newPosition.x += (deltaTime * velocity.x) + (0.5 * resultantAcceleration.x * deltaTime * deltaTime);
		newPosition.y += (deltaTime * velocity.y) + (0.5 * resultantAcceleration.y * deltaTime * deltaTime);
		newPosition.z += (deltaTime * velocity.z) + (0.5 * resultantAcceleration.z * deltaTime * deltaTime);
		newPositions.add(newPosition);
		
		
		planet1.updateVelocity(resultantAcceleration, deltaTime);
		
		// Wait until all planet velocity values have been updated
		
		//planet1.updatePosition(deltaTime);
	}
	
	// For basic Euler integration
	for (Planet planet : planets) {
		planet.updatePosition(deltaTime);
	}
	
	
	// Possible better SUVAT based integration
	// Update planet positions after all the acceleration vectors have been calculated
	int i = 0;
	for (Planet planet : planets) {
		planet.setPosition(newPositions.get(i++));
		//planet.setRotY(getTime() * planet.getVelocity().magnitude());
	}
	
	*/

}
