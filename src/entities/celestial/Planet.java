package entities.celestial;

import entities.Entity;
import math.Vector3f;
import model.Model;

public class Planet extends Entity {
	
	// Mass in kilogrammes
	private float mass;
	
	// Distance from the sun
	private float orbitalRadius;
	
	// Time taken for one complete orbit
	private float timePeriod;
	
	private float initialAngle;
	
	public Planet(Model model, Vector3f position, float orbitalRadius, float timePeriod) {
		this(model, position, orbitalRadius, timePeriod, 0);
	}
	
	public Planet(Model model, Vector3f position, float orbitalRadius, float timePeriod, float initialAngle) {
		super(model, position);
		this.orbitalRadius = orbitalRadius;
		this.timePeriod = timePeriod;
		this.initialAngle = initialAngle;
	}
	
	public void setPositionFromTime(float time) {
		
		Vector3f pos = getPosition();
		
		float numberOfOrbits = time/timePeriod;
		
		pos.x = (float) (orbitalRadius * Math.sin(Math.toRadians(numberOfOrbits * 360 + initialAngle)));
		
		pos.z = (float) (orbitalRadius * Math.cos(Math.toRadians(numberOfOrbits * 360 + initialAngle)));
		
	}
	

}
