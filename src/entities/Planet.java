package entities;

import math.Vector3f;
import model.Model;

public class Planet extends Entity {
	
	private float orbitalRadius;
	private float timePeriod;
	
	public Planet(Model model, Vector3f position, float orbitalRadius, float timePeriod) {
		super(model, position);
		this.orbitalRadius = orbitalRadius;
		this.timePeriod = timePeriod;
	}
	
	public void setPositionFromTime(float time) {
		
		Vector3f pos = getPosition();
		
		float numberOfOrbits = time/timePeriod;
		
		pos.x = (float) (orbitalRadius * Math.sin(Math.toRadians(numberOfOrbits * 360)));
		
		pos.z = (float) (orbitalRadius * Math.cos(Math.toRadians(numberOfOrbits * 360)));
		
	}
	

}
