package entities.celestial;

import entities.Entity;
import math.Vector3f;
import model.Model;

// TODO: Have a base class from which planet and star both inherit

public class Planet extends Entity {
	
	// Mass in kilogrammes
	private float mass;
	
	// Distance from the sun
	private float orbitalRadius;
	
	// Time taken for one complete orbit
	private float timePeriod;
	
	// Starting angle in the planets orbit
	private float initialAngle;
	
	private Vector3f velocity = new Vector3f();
	
	// Constructors
	public Planet(Model model, Vector3f position, float orbitalRadius, float timePeriod) {
		this(model, position, orbitalRadius, timePeriod, 0);
	}
	
	public Planet(Model model, Vector3f position, float orbitalRadius, float timePeriod, float initialAngle) {
		super(model, position);
		this.orbitalRadius = orbitalRadius;
		this.timePeriod = timePeriod;
		this.initialAngle = initialAngle;
	}
	
	
	// Calculate the position in the orbital circle based on the time
	public void setPositionFromTime(float time) {
		
		Vector3f pos = getPosition();
		
		float numberOfOrbits = time/timePeriod;
		
		pos.x = (float) (orbitalRadius * Math.sin(Math.toRadians(numberOfOrbits * 360 + initialAngle)));
		pos.z = (float) (orbitalRadius * Math.cos(Math.toRadians(numberOfOrbits * 360 + initialAngle)));
		
	}
	
	
	public void tick(Vector3f acceleration, float deltaTime) {
		velocity.x += acceleration.x * deltaTime;
		velocity.y += acceleration.y * deltaTime;
		velocity.z += acceleration.z * deltaTime;
		
		Vector3f position = getPosition();
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		position.z += velocity.z * deltaTime;
	}
	
	
	public Vector3f getAccelerationVectorToPlanet(Planet planet) {
		Vector3f pos1 = getPosition();
		Vector3f pos2 = planet.getPosition();
		Vector3f vecToPlanet = new Vector3f(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
		vecToPlanet.normalise();
		vecToPlanet.scale(getForce(planet)/mass);
		return vecToPlanet;
	}
	
	public float getForce(Planet planet) {
		float distance = getDistanceTo(planet);
		return (float) (Constants.G * getMass() * planet.getMass() / distance*distance);
	}
	
	public float getDistanceTo(Planet planet) {
		Vector3f pos1 = getPosition();
		Vector3f pos2 = planet.getPosition();
		// Basic Euclidean distance calculation
		return (float) Math.sqrt(pos1.x*pos2.x + pos1.y*pos2.y + pos1.z*pos2.z);
	}
	
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getMass() {
		return mass;
	}

}
