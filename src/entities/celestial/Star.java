package entities.celestial;

import lighting.LightSource;
import math.geometry.Vector3f;
import model.Model;

public class Star extends CelestialEntity implements LightSource {
	
	// Consider having light as a field here
	Vector3f lightColour = new Vector3f(1, 1, 0.702f);

	public Star(Model model, Vector3f position) {
		super(model, position);
	}
	
	public Star(Model model, Vector3f position, Vector3f velocity) {
		super(model, position, velocity);
	}
	
	
	
	
	@Override
	public Vector3f getLightPosition() {
		return this.getPosition();
	}

	@Override
	public Vector3f getLightColour() {
		return lightColour;
	}

	@Override
	public float getLightIntensity() {
		return 1;
	}
	
}
