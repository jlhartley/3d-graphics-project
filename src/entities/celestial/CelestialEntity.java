package entities.celestial;

import entities.Entity;
import math.geometry.Vector3f;
import model.Model;
import physics.Constants;

public class CelestialEntity extends Entity {
	
	private float mass;
	
	private Vector3f velocity;
	
	// Initialise at origin, with velocity = (0, 0, 0)
	public CelestialEntity(Model model) {
		this(model, new Vector3f());
	}
	
	// Initialise at given position, with a velocity of (0, 0, 0)
	public CelestialEntity(Model model, Vector3f position) {
		this(model, position, new Vector3f());
	}
	
	// Call through to Entity constructor for model and position, 
	// and then set velocity field
	public CelestialEntity(Model model, Vector3f position, Vector3f velocity) {
		super(model, position);
		this.velocity = velocity;
	}
	
	
	
	// Euler integration
	
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
	
	
	
	public Vector3f forceTo(CelestialEntity celestialEntity) {
		
		// F = m * a,  a = F / m
		float forceMagnitude = getForceMagnitude(celestialEntity);
		
		Vector3f pos1 = this.getPosition();
		Vector3f pos2 = celestialEntity.getPosition();
		
		// Subtract position1 from position2 to get the vector between the positions
		// Then scale the vector, such that its magnitude is the calculated value
		Vector3f force = Vector3f.sub(pos2, pos1).setMagnitude(forceMagnitude);
		
		return force;
	}
	
	
	
	
	// Newton's law of universal gravitation
	private float getForceMagnitude(CelestialEntity celestialEntity) {
		float distanceSquared = distanceSquaredTo(celestialEntity);
		// Limit the maximum force by limiting the minimum distance
		if (distanceSquared < 3) {
			distanceSquared = 3;
		}
		return (float) (Constants.G * this.getMass() * celestialEntity.getMass() / distanceSquared);
	}
	
	private float distanceSquaredTo(CelestialEntity celestialEntity) {
		Vector3f pos1 = this.getPosition();
		Vector3f pos2 = celestialEntity.getPosition();
		Vector3f vecToPlanet = Vector3f.sub(pos2, pos1);;
		return vecToPlanet.magnitudeSquared();
	}
	
	public float distanceTo(CelestialEntity celestialEntity) {
		return (float) Math.sqrt(distanceSquaredTo(celestialEntity));
	}
	
	
	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	// Set velocity with provided components
	public void setVelocity(float x, float y, float z) {
		velocity.set(x, y, z);
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getMass() {
		return mass;
	}

}
