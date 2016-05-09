package entities.celestial;

import entities.Entity;
import math.geometry.Vector3f;
import model.Model;
import physics.Constants;

public class CelestialEntity extends Entity {
	
	// Apply dampening to distance calculations, to avoid divide by 0
	private static float EPSILON = 0.001f;
	
	
	private float mass;
	
	private Vector3f velocity;
	
	private Vector3f acceleration;
	
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
		this(model, position, velocity, new Vector3f());
	}
	
	public CelestialEntity(Model model, Vector3f position, Vector3f velocity, Vector3f acceleration) {
		super(model, position);
		this.velocity = velocity;
		this.acceleration = acceleration;
	}
	
	
	
	
	public Vector3f forceTo(CelestialEntity celestialEntity) {
		
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
		float distanceSquared = distanceSquaredTo(celestialEntity) + EPSILON;
		return (float) (Constants.G * this.getMass() * celestialEntity.getMass() / distanceSquared);
	}
	
	private float distanceSquaredTo(CelestialEntity celestialEntity) {
		Vector3f pos1 = this.getPosition();
		Vector3f pos2 = celestialEntity.getPosition();
		Vector3f r = Vector3f.sub(pos2, pos1);;
		return r.magnitudeSquared();
	}
	
	public float distanceTo(CelestialEntity celestialEntity) {
		return (float) Math.sqrt(distanceSquaredTo(celestialEntity));
	}
	
	
	
	public Vector3f getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector3f acceleration) {
		this.acceleration = acceleration;
	}
	
	// Set velocity with provided components
	public void setAcceleration(float x, float y, float z) {
		acceleration.set(x, y, z);
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
	
	
	public float getMass() {
		return mass;
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}

}
