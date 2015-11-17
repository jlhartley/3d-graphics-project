package lighting;

import math.geometry.Vector3f;

public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	
	private float intensity;
	
	
	public Light(Vector3f position, float intensity, Vector3f colour) {
		this.position = position;
		this.intensity = intensity;
		this.colour = colour;
	}
	
	

}
