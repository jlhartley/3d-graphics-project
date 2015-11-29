package lighting;

import math.geometry.Vector3f;

public interface LightSource {
	public Vector3f getLightPosition();
	public Vector3f getLightColour();
	public float getLightIntensity();
}
