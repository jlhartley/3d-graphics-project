package entities.celestial;

import entities.Entity;
import math.geometry.Vector3f;
import model.Model;
import physics.Constants;

// TODO: Have a base class from which planet and star both inherit
// TODO: Should do something about MoveableEntity

public class Planet extends Entity {
	
	// Mass in kilogrammes
	private float mass;
	
	// Velocity in m/s
	private Vector3f velocity;
	
	
	
	// Constructors
	public Planet(Model model, Vector3f position) {
		this(model, position, new Vector3f());
	}
	
	public Planet(Model model, Vector3f position, Vector3f velocity) {
		super(model, position);
		this.velocity = velocity;
	}
	
	
	
	public void updateVelocity(Vector3f acceleration, float deltaTime) {
		velocity.x += acceleration.x * deltaTime;
		velocity.y += acceleration.y * deltaTime;
		velocity.z += acceleration.z * deltaTime;
	}
	
	
	public void updatePosition(float deltaTime) {
		Vector3f position = getPosition();
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		position.z += velocity.z * deltaTime;
	}
	
	
	
	
	
	public Vector3f accelerationVectorTo(Planet planet) {
		
		 // F = m * a,  a = F / m
		float accelerationMagnitude = getForceScalar(planet) / mass;
		
		Vector3f pos1 = this.getPosition();
		Vector3f pos2 = planet.getPosition();
		
		// Subtract position1 from position2 to get the vector between the positions
		Vector3f acceleration = Vector3f.sub(pos2, pos1)
				.setMagnitude(accelerationMagnitude); // Scale the vector
		
		return acceleration;
	}
	
	
	
	
	// Newton's law of universal gravitation
	public float getForceScalar(Planet planet) {
		float distanceSquared = distanceSquaredTo(planet);
		// Limit the maximum force by limiting the minimum distance
		if (distanceSquared < 3) {
			distanceSquared = 3;
		}
		return (float) (Constants.G * this.getMass() * planet.getMass() / distanceSquared);
	}
	
	private float distanceSquaredTo(Planet planet) {
		Vector3f pos1 = this.getPosition();
		Vector3f pos2 = planet.getPosition();
		Vector3f vecToPlanet = Vector3f.sub(pos2, pos1);;
		return vecToPlanet.magnitudeSquared();
	}
	
	public float distance(Planet planet) {
		return (float) Math.sqrt(distanceSquaredTo(planet));
	}
	
	
	
	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getMass() {
		return mass;
	}

}
