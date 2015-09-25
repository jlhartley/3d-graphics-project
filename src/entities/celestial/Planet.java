package entities.celestial;

import entities.Entity;
import math.Vector3f;
import model.Model;

// TODO: Have a base class from which planet and star both inherit
// TODO: Should do something about MoveableEntity

public class Planet extends Entity {
	
	// Mass in kilogrammes
	private float mass;
	
	// Distance from the sun
	//private float orbitalRadius;
	
	// Time taken for one complete orbit
	//private float timePeriod;
	
	// Starting angle in the planets orbit
	//private float initialAngle;
	
	private Vector3f velocity = new Vector3f();
	
	// Constructors
	public Planet(Model model, Vector3f position) {
		this(model, position, new Vector3f());
	}
	
	public Planet(Model model, Vector3f position, Vector3f velocity) {
		super(model, position);
		this.velocity = velocity;
	}
	
	
	// Calculate the position in the orbital circle based on the time
	// OLD
	/*
	public void setPositionFromTime(float time) {
		
		Vector3f pos = getPosition();
		
		float numberOfOrbits = time/timePeriod;
		
		pos.x = (float) (orbitalRadius * Math.sin(Math.toRadians(numberOfOrbits * 360 + initialAngle)));
		pos.z = (float) (orbitalRadius * Math.cos(Math.toRadians(numberOfOrbits * 360 + initialAngle)));
		
	}*/
	
	
	public void tick(Vector3f acceleration, float deltaTime) {
		
		velocity.x += acceleration.x * deltaTime;
		velocity.y += acceleration.y * deltaTime;
		velocity.z += acceleration.z * deltaTime;
		
		Vector3f position = getPosition();
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		position.z += velocity.z * deltaTime;
	}
	
	
	
	// Mutable Vector vs static Vector issue
	public Vector3f getAccelerationVectorToPlanet(Planet planet) {
		
		Vector3f pos1 = getPosition();
		Vector3f pos2 = planet.getPosition();
		
		Vector3f vecToPlanet = new Vector3f(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
		
		
		vecToPlanet.normalise();
		vecToPlanet.scale(getForce(planet) / mass); // F = ma, a = F / m
		return vecToPlanet;
	}
	
	
	public float getForce(Planet planet) {
		return (float) (Constants.G * this.getMass() * planet.getMass() / distanceSquared(planet));
	}
	
	/*public float getForce(Planet planet) {
		float distance = getDistanceTo(planet);
		return (float) (Constants.G * getMass() * planet.getMass() / distance*distance);
	}*/
	
	
	public float distanceSquared(Planet planet) {
		Vector3f pos1 = this.getPosition();
		Vector3f pos2 = planet.getPosition();
		Vector3f vecToPlanet = new Vector3f(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
		return vecToPlanet.magnitudeSquared();
	}
	
	public float distance(Planet planet) {
		return distanceSquared(planet);
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
